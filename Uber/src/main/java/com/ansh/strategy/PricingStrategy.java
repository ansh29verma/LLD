package com.ansh.strategy;


import com.ansh.entity.Location;

public interface PricingStrategy {
    double calculateSurge(double baseFare, Location source, Location destination);
}
