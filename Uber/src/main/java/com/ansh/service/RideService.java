package com.ansh.service;

// File: com/uber/service/RideService.java

import com.ansh.entity.*;
import com.ansh.exceptions.FareExpiredException;
import com.ansh.exceptions.NoDriverAvailableException;
import com.ansh.repo.FareRepository;
import com.ansh.repo.RideRepository;
import com.ansh.repo.RiderRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RideService {
    private final RideRepository rideRepository;
    private final FareRepository fareRepository;
    private final DriverMatchingService driverMatchingService;
    private final RiderRepository riderRepository;

    public Ride requestRide(String rideId, String riderId, String fareId, Product product, Location src, Location dest) {
        Rider rider = riderRepository.findById(riderId);
        if (rider == null) {
            throw new IllegalArgumentException("Rider not found: " + riderId);
        }

        Fare fare = fareRepository.getValidFare(fareId, riderId);
        if (fare == null) {
            throw new FareExpiredException("Price quote expired or belongs to another customer. Fetch a new quote.");
        }

        Driver driver = driverMatchingService.assignDriver(product, src);
        if (driver == null) {
            throw new NoDriverAvailableException("No compatible drivers available at this moment.");
        }

        Ride ride = new Ride(rideId, product, src, dest, rider, driver, driver.getVehicle(), fare.getAmount());
        rideRepository.save(ride);
        return ride;
    }

    public void startTrip(String rideId) {
        Ride ride = getRideOrThrow(rideId);
        ride.start();
    }

    public void completeTrip(String rideId) {
        Ride ride = getRideOrThrow(rideId);
        ride.complete();
    }

    public void cancelTrip(String rideId) {
        Ride ride = getRideOrThrow(rideId);
        ride.cancel();
    }

    private Ride getRideOrThrow(String rideId) {
        Ride ride = rideRepository.findById(rideId);
        if (ride == null) {
            throw new IllegalArgumentException("Ride does not exist: " + rideId);
        }
        return ride;
    }
}