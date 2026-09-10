package com.ansh.strategy;

// File: com/uber/strategy/NearestDriverMatchingStrategy.java

import com.ansh.entity.Driver;
import com.ansh.entity.Location;

import java.util.Comparator;
import java.util.List;

public class NearestDriverMatchingStrategy implements DriverMatchingStrategy {
    @Override
    public Driver matchDriver(List<Driver> candidateDrivers, Location pickupLocation) {
        List<Driver> sorted = candidateDrivers.stream()
                .sorted(Comparator.comparingDouble(d -> d.getCurrentLocation().distanceTo(pickupLocation)))
                .toList();

        for (Driver driver : sorted) {
            if (driver.markUnavailable()) {
                return driver;
            }
        }
        return null;
    }
}