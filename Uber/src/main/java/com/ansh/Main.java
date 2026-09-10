package com.ansh;




import com.ansh.entity.*;
import com.ansh.exceptions.InvalidStateTransitionException;
import com.ansh.repo.DriverRepository;
import com.ansh.repo.FareRepository;
import com.ansh.repo.RideRepository;
import com.ansh.repo.RiderRepository;
import com.ansh.service.DriverMatchingService;
import com.ansh.service.FareEstimationService;
import com.ansh.service.RideService;
import com.ansh.strategy.DriverMatchingStrategy;
import com.ansh.strategy.NearestDriverMatchingStrategy;
import com.ansh.strategy.NightPricingStrategy;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // 1. Initialize Repositories
        DriverRepository driverRepo = new DriverRepository();
        RiderRepository riderRepo = new RiderRepository();
        FareRepository fareRepo = new FareRepository(3000); // 3 seconds TTL
        RideRepository rideRepo = new RideRepository();

        DriverMatchingStrategy driverMatchingStrategy = new NearestDriverMatchingStrategy();

        // 2. Initialize Services & Strategies
        DriverMatchingService matchingService = new DriverMatchingService(
                driverRepo,driverMatchingStrategy
        );
        FareEstimationService fareService = new FareEstimationService(fareRepo, new NightPricingStrategy());
        RideService rideService = new RideService(rideRepo, fareRepo, matchingService, riderRepo);

        // 3. Register Entities
        Product uberAuto = new UberAuto("PROD_AUTO");
        Product uberGo = new UberGo("PROD_GO");
        Product uberX = new UberX("PROD_X");

        Rider rider = new Rider("RIDER_01", "Anshul");
        riderRepo.save(rider);

        Vehicle sedan = new Vehicle("KA-01-EQ-5544", List.of(uberGo, uberX));
        Driver driver = new Driver("DRV_01", "Vikram", new Location(12.9716, 77.5946), sedan, 4.9);
        driverRepo.save(driver);

        Location pickup = new Location(12.9720, 77.5950);
        Location destination = new Location(12.9352, 77.6245);

        // 4. Lock Fare Quote
        Fare fare = fareService.lockFare("FARE_101", rider.getId(), uberGo, pickup, destination);
        System.out.println("Fare locked: INR " + fare.getAmount());

        // 5. Booking & State Machine Execution
        Ride ride = rideService.requestRide("RIDE_999", rider.getId(), fare.getId(), uberGo, pickup, destination);
        System.out.println("Current Ride Status: " + ride.getStatus()); // ACCEPTED

        // Illegal transition check: Complete without starting
        try {
            ride.complete();
        } catch (InvalidStateTransitionException ex) {
            System.out.println("[State Guard Caught] " + ex.getMessage());
        }

        // Proper state progression
        rideService.startTrip(ride.getId());
        System.out.println("Current Ride Status: " + ride.getStatus()); // IN_PROGRESS

        rideService.completeTrip(ride.getId());
        System.out.println("Current Ride Status: " + ride.getStatus()); // COMPLETED
        System.out.println("Driver Availability Post-Ride: " + driver.isAvailable()); // true

        fareRepo.shutdown();
    }
}