package com.ansh.service;

// File: com/uber/service/FareEstimationService.java


import com.ansh.entity.Fare;
import com.ansh.entity.Location;
import com.ansh.entity.Product;
import com.ansh.repo.FareRepository;
import com.ansh.strategy.PricingStrategy;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class FareEstimationService {
    private final FareRepository fareRepository;
    private final PricingStrategy pricingStrategy;

    public Map<Product, Double> estimateFares(Location source, Location destination, List<Product> products) {
        double distanceKm = source.distanceTo(destination);
        double durationMin = distanceKm * 2.0;

        Map<Product, Double> estimates = new LinkedHashMap<>();
        for (Product product : products) {
            double baseFare = product.calculateBaseFare(distanceKm, durationMin);
            double surge = pricingStrategy.calculateSurge(baseFare, source, destination);
            estimates.put(product, baseFare + surge);
        }
        return estimates;
    }

    public Fare lockFare(String fareId, String riderId, Product product, Location source, Location destination) {
        double distanceKm = source.distanceTo(destination);
        double durationMin = distanceKm * 2.0;

        double baseFare = product.calculateBaseFare(distanceKm, durationMin);
        double surge = pricingStrategy.calculateSurge(baseFare, source, destination);

        Fare fare = Fare.builder()
                .id(fareId)
                .product(product)
                .source(source)
                .destination(destination)
                .amount(baseFare + surge)
                .build();

        fareRepository.save(fare, riderId);
        return fare;
    }
}