package com.ansh.strategy;

// File: com/uber/strategy/HighestRatedMatchingStrategy.java

import com.ansh.entity.Driver;
import com.ansh.entity.Location;

import java.util.Comparator;
import java.util.List;

public class HighestRatedMatchingStrategy implements DriverMatchingStrategy {
    @Override
    public Driver matchDriver(List<Driver> candidateDrivers, Location pickupLocation) {
        List<Driver> sorted = candidateDrivers.stream()
                .sorted(Comparator.comparingDouble(Driver::getRating).reversed())
                .toList();

        for (Driver driver : sorted) {
            if (driver.markUnavailable()) {
                return driver;
            }
        }
        return null;
    }
}