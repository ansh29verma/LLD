package com.ansh.entity;

// File: com/uber/model/Product.java


import com.ansh.enums.ProductType;

public interface Product {
    String getId();
    ProductType getType();
    double getBaseRate();
    double getPerKmRate();
    double getPerMinuteRate();

    default double calculateBaseFare(double distanceKm, double durationMin) {
        return getBaseRate() + (getPerKmRate() * distanceKm) + (getPerMinuteRate() * durationMin);
    }
}