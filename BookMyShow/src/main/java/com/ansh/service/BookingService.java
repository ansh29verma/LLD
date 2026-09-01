package com.ansh.service;

import com.ansh.entity.Booking;
import com.ansh.entity.Seat;
import com.ansh.enums.BookingStatus;
import com.ansh.enums.PaymentType;
import com.ansh.exceptions.SeatNotAvailableException;
import com.ansh.locking.LockProvider;
import com.ansh.repo.BookingRepository;
import com.ansh.strategy.PaymentStrategy;
import com.ansh.strategy.PaymentStrategyFactory;

import java.util.ArrayList;
import java.util.List;

public class BookingService {
    private final LockProvider lockProvider;
    private final BookingRepository bookingRepository;
    private final long ttlMs;

    public BookingService(LockProvider lockProvider, BookingRepository bookingRepository, long ttlMs) {
        this.lockProvider = lockProvider;
        this.bookingRepository = bookingRepository;
        this.ttlMs = ttlMs;
    }

    public Booking createBooking(String id, String userId, String showId, List<Seat> seats) {
        // 1. Check if any seat is already booked in a CONFIRMED booking
        for (Booking existingBooking : bookingRepository.getAll()) {
            if (existingBooking.getShowId().equals(showId) && existingBooking.getStatus() == BookingStatus.CONFIRMED) {
                for (Seat seat : seats) {
                    if (existingBooking.getSeats().stream().anyMatch(s -> s.getId().equals(seat.getId()))) {
                        throw new SeatNotAvailableException("Seat " + seat.getId() + " is already booked.");
                    }
                }
            }
        }

        // 2. Try to lock all seats
        List<String> lockedKeys = new ArrayList<>();
        for (Seat seat : seats) {
            String key = showId + "_" + seat.getId();
            if (lockProvider.tryLock(key, ttlMs, userId)) {
                lockedKeys.add(key);
            } else {
                // Rollback already locked seats in this attempt to avoid deadlocks or partial locks
                for (String lockedKey : lockedKeys) {
                    lockProvider.unlock(lockedKey);
                }
                throw new SeatNotAvailableException("Seat " + seat.getId() + " is temporarily unavailable.");
            }
        }

        // 3. Compute total price
        double amount = 0;
        for (Seat seat : seats) {
            amount += seat.getPrice();
        }

        // 4. Create and save Booking
        Booking booking = new Booking(id, userId, showId, seats, amount);
        bookingRepository.save(booking);
        return booking;
    }

    public void confirmBooking(String bookingId, PaymentType paymentType) {
        Booking booking = bookingRepository.get(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found");
        }
        if (booking.getStatus() != BookingStatus.CREATED) {
            throw new IllegalStateException("Booking is not in CREATED state");
        }

        // Validate locks
        for (Seat seat : booking.getSeats()) {
            String key = booking.getShowId() + "_" + seat.getId();
            if (lockProvider.isLockExpired(key) || !lockProvider.isLockedBy(key, booking.getUserId())) {
                booking.setStatus(BookingStatus.FAILED);
                bookingRepository.save(booking);
                throw new SeatNotAvailableException("Seat " + seat.getId() + " is not locked or lock has expired/not owned by you.");
            }
        }

        // Process payment
        PaymentStrategy paymentStrategy = PaymentStrategyFactory.getPaymentStrategy(paymentType);
        boolean paymentSuccess = paymentStrategy.pay(booking);

        if (paymentSuccess) {
            booking.setStatus(BookingStatus.CONFIRMED);
            booking.setPaymentType(paymentType);
            bookingRepository.save(booking);
            // Unlock seats as they are now permanently booked (clearing temporary lock state)
            for (Seat seat : booking.getSeats()) {
                String key = booking.getShowId() + "_" + seat.getId();
                lockProvider.unlock(key);
            }
            System.out.println("[Booking Service] Booking " + bookingId + " confirmed successfully!");
        } else {
            booking.setStatus(BookingStatus.FAILED);
            bookingRepository.save(booking);
            System.out.println("[Booking Service] Booking " + bookingId + " failed due to payment failure.");
        }
    }
}
