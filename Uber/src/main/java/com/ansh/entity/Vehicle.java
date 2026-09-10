package com.ansh.entity;

// File: com/uber/model/Vehicle.java


import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class Vehicle {
    private final String licensePlate;
    private final List<Product> supportedProducts;

    public boolean supportsProduct(Product product) {
        return supportedProducts.stream()
                .anyMatch(p -> p.getType() == product.getType());
    }
}