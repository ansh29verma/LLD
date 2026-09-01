package com.ansh;



import com.ansh.dto.FitnessClassResponseDto;
import com.ansh.entity.Booking;
import com.ansh.entity.User;
import com.ansh.enums.ClassType;
import com.ansh.enums.UserTier;
import com.ansh.repo.*;
import com.ansh.service.BookingService;
import com.ansh.service.BookingServiceImpl;
import com.ansh.strategy.StrictThirtyMinuteCancellationPolicy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Initialize Components
        UserRepository userRepository = new InMemoryUserRepository();
        FitnessClassRepository classRepository = new InMemoryFitnessClassRepository();
        BookingRepository bookingRepository = new InMemoryBookingRepository();

        BookingService bookingService = new BookingServiceImpl(
                userRepository, classRepository, bookingRepository, new StrictThirtyMinuteCancellationPolicy()
        );

        System.out.println("--- 1. Registering Users ---");
        User alice = bookingService.registerUser("U1", "Alice", "alice@test.com", UserTier.SILVER); // Limit: 3
        User bob = bookingService.registerUser("U2", "Bob", "bob@test.com", UserTier.GOLD);      // Limit: 5
        User charlie = bookingService.registerUser("U3", "Charlie", "charlie@test.com", UserTier.PLATINUM); // Limit: 10
        System.out.println("Registered Alice (Silver), Bob (Gold), Charlie (Platinum)");

        System.out.println("\n--- 2. Creating Classes ---");
        LocalDateTime classTime = LocalDateTime.now().plusHours(2);
        bookingService.createClass("C1", "Morning Yoga", ClassType.YOGA, 2, classTime, classTime.plusHours(1));
        System.out.println("Created Morning Yoga (Capacity: 2)");

        System.out.println("\n--- 3. Testing Capacity & Waitlisting ---");
        Booking b1 = bookingService.bookClass("U1", "C1");
        System.out.println("Alice booked C1 -> Status: " + b1.getStatus());

        Booking b2 = bookingService.bookClass("U2", "C1");
        System.out.println("Bob booked C1 -> Status: " + b2.getStatus());

        Booking b3 = bookingService.bookClass("U3", "C1");
        System.out.println("Charlie booked C1 -> Status: " + b3.getStatus() + " (Expect WAITLISTED)");

        System.out.println("\n--- 4. Testing Cancellation & Auto Promotion ---");
        System.out.println("Alice cancels booking...");
        bookingService.cancelBooking("U1", "C1", LocalDateTime.now());

        List<Booking> charlieBookings = bookingService.getUserBookings("U3");
        System.out.println("Charlie's updated status for C1: " + charlieBookings.get(0).getStatus());

        System.out.println("\n--- 5. Testing Concurrency (Thread-Safe Concurrent Booking) ---");
        // Create small capacity class and simulate multi-thread surge
        bookingService.createClass("C2", "HIIT Blast", ClassType.GYM, 1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1));

        // Register 5 concurrent users
        for (int i = 4; i <= 8; i++) {
            bookingService.registerUser("U" + i, "User" + i, "user" + i + "@test.com", UserTier.GOLD);
        }

        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 4; i <= 8; i++) {
            final String userId = "U" + i;
            executor.submit(() -> {
                try {
                    Booking b = bookingService.bookClass(userId, "C2");
                    System.out.println("Concurrent booking by " + userId + " -> " + b.getStatus());
                } catch (Exception e) {
                    System.out.println("Concurrent booking error for " + userId + ": " + e.getMessage());
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);

        System.out.println("\n--- Final Class Status ---");
        List<FitnessClassResponseDto> classes = bookingService.getAllClasses();
        for (FitnessClassResponseDto dto : classes) {
            System.out.println("Class: " + dto.getTitle() + " | Confirmed: " + dto.getConfirmedBookingsCount() + " | Waitlist: " + dto.getWaitlistCount());
        }
    }
}