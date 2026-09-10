package com.ansh.entity;

// File: com/uber/model/UberAuto.java

import com.ansh.enums.ProductType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UberAuto implements Product {
    private final String id;
    @Override public ProductType getType() { return ProductType.UBER_AUTO; }
    @Override public double getBaseRate() { return 30.0; }
    @Override public double getPerKmRate() { return 8.0; }
    @Override public double getPerMinuteRate() { return 1.5; }
}