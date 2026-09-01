package com.ansh;

import com.ansh.entity.*;
import com.ansh.enums.PaymentType;
import com.ansh.locking.InMemoryLockProvider;
import com.ansh.repo.BookingRepository;
import com.ansh.repo.MovieRepository;
import com.ansh.repo.ShowRepository;
import com.ansh.repo.TheaterRepository;
import com.ansh.service.BookingService;
import com.ansh.service.MovieService;
import com.ansh.service.ShowService;
import com.ansh.service.TheaterService;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("==================================================");
        System.out.println("  INITIALIZING BOOKMYSHOW LLD SYSTEM DEMO");
        System.out.println("==================================================\n");

        // 1. Infrastructure Setup
        MovieRepository movieRepo = new MovieRepository();
        ShowRepository showRepo = new ShowRepository();
        TheaterRepository theaterRepo = new TheaterRepository();
        BookingRepository bookingRepo = new BookingRepository();

        MovieService movieService = new MovieService(movieRepo);
        ShowService showService = new ShowService(showRepo);
        TheaterService theaterService = new TheaterService(theaterRepo);

        // TTL is set to 5 seconds (5000 ms) for quick verification of demo 4
        long lockTtlMs = 5000;
        InMemoryLockProvider lockProvider = new InMemoryLockProvider();
        BookingService bookingService = new BookingService(lockProvider, bookingRepo, lockTtlMs);

        // 2. Data Seed: Theater, Screens, Seats, Movies, Shows
        Theater theater1 = theaterService.createTheater("theater-1", "PVR Cinemas, Mall of India");

        Screen screen1 = new Screen("screen-1");
        theaterService.addScreenToTheater("theater-1", screen1);

        // Add 10 seats (Regular seats 1-5, Recliner seats 6-10)
        for (int i = 1; i <= 5; i++) {
            theaterService.addSeatToScreen("theater-1", "screen-1", new RegularSeat("seat-" + i));
        }
        for (int i = 6; i <= 10; i++) {
            theaterService.addSeatToScreen("theater-1", "screen-1", new ReclinerSeat("seat-" + i));
        }

        Movie interstellar = movieService.createMovie("movie-1", "Interstellar", 120);
        Show show1 = showService.createShow("show-1", interstellar, "6:30 PM", theater1, screen1);

        // --------------------------------------------------
        // DEMO 1: List all shows by Movie Name
        // --------------------------------------------------
        System.out.println("--------------------------------------------------");
        System.out.println("[DEMO 1] Listing all shows for movie 'Interstellar'");
        System.out.println("--------------------------------------------------");
        List<Show> activeShows = showService.getShowsByMovieTitle("Interstellar");
        for (Show s : activeShows) {
            System.out.println("Show ID: " + s.getId() +
                    " | Movie: " + s.getMovie().getTitle() +
                    " | Theater: " + s.getTheater().getName() +
                    " | Screen: " + s.getScreen().getId() +
                    " | Timing: " + s.getStartTimeStr());
        }
        System.out.println();

        // --------------------------------------------------
        // DEMO 2: Normal booking flow (User 1)
        // --------------------------------------------------
        System.out.println("--------------------------------------------------");
        System.out.println("[DEMO 2] Regular booking and payment flow for User 1");
        System.out.println("--------------------------------------------------");
        List<Seat> user1Seats = Arrays.asList(screen1.getSeat("seat-1"), screen1.getSeat("seat-2"));
        try {
            System.out.println("[User 1] Selecting seat-1 and seat-2...");
            Booking booking1 = bookingService.createBooking("booking-1", "user-1", "show-1", user1Seats);
            System.out.println("[User 1] Booking created. Booking ID: " + booking1.getId() +
                    " | Status: " + booking1.getStatus() +
                    " | Total Price: INR " + booking1.getAmount());

            System.out.println("[User 1] Initiating payment...");
            bookingService.confirmBooking("booking-1", PaymentType.CARD);
        } catch (Exception e) {
            System.err.println("Error in User 1 booking: " + e.getMessage());
        }
        System.out.println();

        // --------------------------------------------------
        // DEMO 3: Concurrency - Overlapping seats (User 2 vs User 3)
        // --------------------------------------------------
        System.out.println("--------------------------------------------------");
        System.out.println("[DEMO 3] Concurrency: Overlapping seat requests");
        System.out.println("--------------------------------------------------");
        // User 2 selects seat-3 and seat-4
        // User 3 selects seat-4 and seat-5 (seat-4 is overlapping)
        List<Seat> user2Seats = Arrays.asList(screen1.getSeat("seat-3"), screen1.getSeat("seat-4"));
        List<Seat> user3Seats = Arrays.asList(screen1.getSeat("seat-4"), screen1.getSeat("seat-5"));

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<Void> taskUser2 = () -> {
            try {
                System.out.println("[User 2] Requesting lock for seat-3 and seat-4...");
                Booking booking2 = bookingService.createBooking("booking-2", "user-2", "show-1", user2Seats);
                System.out.println("[User 2] Booking successfully created! Proceeding to payment...");
                bookingService.confirmBooking("booking-2", PaymentType.CARD);
            } catch (Exception e) {
                System.out.println("[User 2] Failed: " + e.getMessage());
            }
            return null;
        };

        Callable<Void> taskUser3 = () -> {
            try {
                System.out.println("[User 3] Requesting lock for seat-4 and seat-5...");
                Booking booking3 = bookingService.createBooking("booking-3", "user-3", "show-1", user3Seats);
                System.out.println("[User 3] Booking successfully created! Proceeding to payment...");
                bookingService.confirmBooking("booking-3", PaymentType.UPI);
            } catch (Exception e) {
                System.out.println("[User 3] Failed: " + e.getMessage());
            }
            return null;
        };

        executor.invokeAll(Arrays.asList(taskUser2, taskUser3));
        executor.shutdown();
        System.out.println();

        // --------------------------------------------------
        // DEMO 4: Lock Expiry after TTL (User 4 vs User 5)
        // --------------------------------------------------
        System.out.println("--------------------------------------------------");
        System.out.println("[DEMO 4] Lock Expiry & TTL violation");
        System.out.println("--------------------------------------------------");
        // User 4 selects seat-6 and seat-7
        List<Seat> user4Seats = Arrays.asList(screen1.getSeat("seat-6"), screen1.getSeat("seat-7"));

        try {
            System.out.println("[User 4] Locking seat-6 and seat-7...");
            Booking booking4 = bookingService.createBooking("booking-4", "user-4", "show-1", user4Seats);
            System.out.println("[User 4] Booking created successfully. Under payment page, waiting...");

            // Sleep for 6 seconds, which is longer than TTL (5 seconds)
            System.out.println("Sleeping for 6 seconds (TTL is 5s)...");
            Thread.sleep(6000);

            // Now, User 5 comes and tries to book the same seats since User 4's lock expired
            System.out.println("[User 5] Trying to book seat-6 and seat-7 (User 4's lock should have expired)...");
            List<Seat> user5Seats = Arrays.asList(screen1.getSeat("seat-6"), screen1.getSeat("seat-7"));
            Booking booking5 = bookingService.createBooking("booking-5", "user-5", "show-1", user5Seats);
            System.out.println("[User 5] Booking created successfully! Lock acquired by User 5.");

            // Now, User 4 finally attempts to complete the payment
            System.out.println("[User 4] Attempting to pay now...");
            try {
                bookingService.confirmBooking("booking-4", PaymentType.UPI);
            } catch (Exception e) {
                System.out.println("[User 4] Payment Failed: " + e.getMessage());
            }

            // User 5 completes payment successfully
            System.out.println("[User 5] Attempting to pay now...");
            bookingService.confirmBooking("booking-5", PaymentType.UPI);

        } catch (Exception e) {
            System.err.println("Unexpected error in Demo 4: " + e.getMessage());
        }

        // Shutdown the scheduled sweeper in the lock provider
        lockProvider.shutdown();
        System.out.println("\n==================================================");
        System.out.println("  BOOKMYSHOW LLD DEMO COMPLETED SUCCESSFULLY");
        System.out.println("==================================================");
    }

}