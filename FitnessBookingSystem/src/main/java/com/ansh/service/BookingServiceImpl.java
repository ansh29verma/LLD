package com.ansh.service;



import com.ansh.dto.FitnessClassResponseDto;
import com.ansh.entity.Booking;
import com.ansh.entity.FitnessClass;
import com.ansh.entity.User;
import com.ansh.enums.BookingStatus;
import com.ansh.enums.ClassType;
import com.ansh.enums.ErrorCode;
import com.ansh.enums.UserTier;
import com.ansh.exception.FitnessBookingException;
import com.ansh.repo.BookingRepository;
import com.ansh.repo.FitnessClassRepository;
import com.ansh.repo.UserRepository;
import com.ansh.strategy.CancellationPolicy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;
    private final FitnessClassRepository classRepository;
    private final BookingRepository bookingRepository;
    private final CancellationPolicy cancellationPolicy;

    public BookingServiceImpl(UserRepository userRepository,
                              FitnessClassRepository classRepository,
                              BookingRepository bookingRepository,
                              CancellationPolicy cancellationPolicy) {
        this.userRepository = userRepository;
        this.classRepository = classRepository;
        this.bookingRepository = bookingRepository;
        this.cancellationPolicy = cancellationPolicy;
    }

    @Override
    public User registerUser(String userId, String name, String email, UserTier tier) {
        if (userRepository.findById(userId).isPresent()) {
            throw new FitnessBookingException(ErrorCode.USER_ALREADY_EXISTS);
        }
        User user = User.builder()
                .userId(userId)
                .name(name)
                .email(email)
                .tier(tier)
                .build();
        return userRepository.save(user);
    }

    @Override
    public FitnessClassResponseDto createClass(String classId, String title, ClassType classType, int capacity, LocalDateTime startTime, LocalDateTime endTime) {
        if (classRepository.findById(classId).isPresent()) {
            throw new FitnessBookingException(ErrorCode.CLASS_ALREADY_EXISTS);
        }
        FitnessClass fitnessClass = FitnessClass.builder()
                .classId(classId)
                .title(title)
                .classType(classType)
                .capacity(capacity)
                .startTime(startTime)
                .endTime(endTime)
                .build();

        classRepository.save(fitnessClass);
        return FitnessClassResponseDto.fromEntity(fitnessClass);
    }

    @Override
    public Booking bookClass(String userId, String classId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new FitnessBookingException(ErrorCode.USER_NOT_FOUND));

        FitnessClass fitnessClass = classRepository.findById(classId)
                .orElseThrow(() -> new FitnessBookingException(ErrorCode.CLASS_NOT_FOUND));

        if (fitnessClass.isCancelled()) {
            throw new FitnessBookingException(ErrorCode.CLASS_ALREADY_CANCELLED);
        }

        // Validate booking quota capacity first using atomic operations
        boolean quotaGranted = user.incrementBookingIfPermitted();
        if (!quotaGranted) {
            throw new FitnessBookingException(ErrorCode.BOOKING_LIMIT_EXCEEDED,
                    "User reached max package limit of " + user.getTier().getMaxBookingLimit() + " classes");
        }

        Booking booking;
        // Granular Synchronization per fitness class object for concurrent reservation handling
        synchronized (fitnessClass) {
            if (fitnessClass.getConfirmedUserIds().contains(userId) || fitnessClass.getWaitlistedUserIds().contains(userId)) {
                user.decrementBookingCount(); // Rollback quota reservation
                throw new FitnessBookingException(ErrorCode.ALREADY_BOOKED_OR_WAITLISTED);
            }

            if (fitnessClass.getConfirmedUserIds().size() < fitnessClass.getCapacity()) {
                fitnessClass.getConfirmedUserIds().add(userId);
                booking = Booking.builder()
                        .bookingId(UUID.randomUUID().toString())
                        .userId(userId)
                        .classId(classId)
                        .status(BookingStatus.CONFIRMED)
                        .bookedAt(LocalDateTime.now())
                        .build();
            } else {
                fitnessClass.getWaitlistedUserIds().add(userId);
                booking = Booking.builder()
                        .bookingId(UUID.randomUUID().toString())
                        .userId(userId)
                        .classId(classId)
                        .status(BookingStatus.WAITLISTED)
                        .bookedAt(LocalDateTime.now())
                        .build();
            }
            classRepository.save(fitnessClass);
        }

        bookingRepository.save(booking);
        return booking;
    }

    @Override
    public void cancelBooking(String userId, String classId, LocalDateTime currentTime) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new FitnessBookingException(ErrorCode.USER_NOT_FOUND));

        FitnessClass fitnessClass = classRepository.findById(classId)
                .orElseThrow(() -> new FitnessBookingException(ErrorCode.CLASS_NOT_FOUND));

        Booking booking = bookingRepository.findByUserIdAndClassId(userId, classId)
                .filter(b -> b.getStatus() != BookingStatus.CANCELLED)
                .orElseThrow(() -> new FitnessBookingException(ErrorCode.BOOKING_NOT_FOUND));

        // Evaluate cancellation policy
        if (!cancellationPolicy.isCancellationAllowed(fitnessClass.getStartTime(), currentTime)) {
            throw new FitnessBookingException(ErrorCode.CANCELLATION_WINDOW_EXPIRED);
        }

        synchronized (fitnessClass) {
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);

            if (fitnessClass.getConfirmedUserIds().remove(userId)) {
                user.decrementBookingCount(); // Restore user quota

                // Promote next user from waitlist if available
                if (!fitnessClass.getWaitlistedUserIds().isEmpty()) {
                    String promotedUserId = fitnessClass.getWaitlistedUserIds().poll();
                    fitnessClass.getConfirmedUserIds().add(promotedUserId);

                    Booking waitlistBooking = bookingRepository.findByUserIdAndClassId(promotedUserId, classId)
                            .orElseThrow(() -> new FitnessBookingException(ErrorCode.BOOKING_NOT_FOUND));

                    waitlistBooking.setStatus(BookingStatus.CONFIRMED);
                    bookingRepository.save(waitlistBooking);
                    System.out.println(">>> User " + promotedUserId + " auto-promoted from WAITLIST to CONFIRMED for class " + classId);
                }
            } else if (fitnessClass.getWaitlistedUserIds().remove(userId)) {
                user.decrementBookingCount(); // Restore user quota
            }

            classRepository.save(fitnessClass);
        }
    }

    @Override
    public void cancelClassByAdmin(String classId) {
        FitnessClass fitnessClass = classRepository.findById(classId)
                .orElseThrow(() -> new FitnessBookingException(ErrorCode.CLASS_NOT_FOUND));

        synchronized (fitnessClass) {
            fitnessClass.setCancelled(true);

            // Refund quotas to all confirmed and waitlisted users
            for (String uid : fitnessClass.getConfirmedUserIds()) {
                userRepository.findById(uid).ifPresent(User::decrementBookingCount);
                bookingRepository.findByUserIdAndClassId(uid, classId).ifPresent(b -> {
                    b.setStatus(BookingStatus.CANCELLED);
                    bookingRepository.save(b);
                });
            }

            for (String uid : fitnessClass.getWaitlistedUserIds()) {
                userRepository.findById(uid).ifPresent(User::decrementBookingCount);
                bookingRepository.findByUserIdAndClassId(uid, classId).ifPresent(b -> {
                    b.setStatus(BookingStatus.CANCELLED);
                    bookingRepository.save(b);
                });
            }

            fitnessClass.getConfirmedUserIds().clear();
            fitnessClass.getWaitlistedUserIds().clear();
            classRepository.save(fitnessClass);
        }
    }

    @Override
    public List<FitnessClassResponseDto> getAllClasses() {
        return classRepository.findAll().stream()
                .map(FitnessClassResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> getUserBookings(String userId) {
        return bookingRepository.findByUserId(userId);
    }
}