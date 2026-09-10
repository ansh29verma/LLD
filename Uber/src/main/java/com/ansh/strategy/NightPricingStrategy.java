package com.ansh.strategy;

// File: com/uber/strategy/NightPricingStrategy.java

import com.ansh.entity.Location;

import java.time.LocalTime;

public class NightPricingStrategy implements PricingStrategy {
    @Override
    public double calculateSurge(double baseFare, Location source, Location destination) {
        int currentHour = LocalTime.now().getHour();
        if (currentHour >= 22 || currentHour < 6) {
            return 50.0;
        }
        return 0.0;
    }
}