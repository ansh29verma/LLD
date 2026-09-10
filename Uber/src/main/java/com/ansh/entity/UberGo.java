package com.ansh.entity;

import com.ansh.enums.ProductType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UberGo implements Product {
    private final String id;
    @Override public ProductType getType() { return ProductType.UBER_GO; }
    @Override public double getBaseRate() { return 50.0; }
    @Override public double getPerKmRate() { return 12.0; }
    @Override public double getPerMinuteRate() { return 2.0; }
}