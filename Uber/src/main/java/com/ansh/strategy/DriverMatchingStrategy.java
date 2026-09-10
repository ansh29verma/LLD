package com.ansh.strategy;

// File: com/uber/strategy/DriverMatchingStrategy.java



import com.ansh.entity.Driver;
import com.ansh.entity.Location;

import java.util.List;

public interface DriverMatchingStrategy {
    Driver matchDriver(List<Driver> candidateDrivers, Location pickupLocation);
}
