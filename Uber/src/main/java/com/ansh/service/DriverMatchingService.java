package com.ansh.service;

// File: com/uber/service/DriverMatchingService.java

import com.ansh.entity.Driver;
import com.ansh.entity.Location;
import com.ansh.entity.Product;
import com.ansh.repo.DriverRepository;
import com.ansh.strategy.DriverMatchingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@RequiredArgsConstructor
public class DriverMatchingService {
    private final DriverRepository driverRepository;
    private final DriverMatchingStrategy matchingStrategy;

    public Driver assignDriver(Product product, Location pickup) {
        List<Driver> candidatePool = driverRepository.findAll().stream()
                .filter(Driver::isAvailable)
                .filter(driver -> driver.getVehicle().supportsProduct(product))
                .toList();

        if (candidatePool.isEmpty()) {
            return null;
        }
        return matchingStrategy.matchDriver(candidatePool, pickup);
    }
}