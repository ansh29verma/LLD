package com.ansh.entity;


import com.ansh.enums.ProductType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UberX implements Product {
    private final String id;
    @Override public ProductType getType() { return ProductType.UBER_X; }
    @Override public double getBaseRate() { return 80.0; }
    @Override public double getPerKmRate() { return 18.0; }
    @Override public double getPerMinuteRate() { return 3.0; }
}