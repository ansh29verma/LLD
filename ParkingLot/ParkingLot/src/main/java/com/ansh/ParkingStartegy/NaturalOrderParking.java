package com.ansh.ParkingStartegy;

import com.ansh.ParkingSpots.ParkingSpot;

import java.util.List;

/**
 * FIX: Changed `extends ParkingStrategy` to `implements ParkingStrategy`
 *      following the interface change.
 * Assigns the first available spot in natural (insertion) order.
 */
public class NaturalOrderParking implements ParkingStrategy {

    @Override
    public ParkingSpot park(List<ParkingSpot> availableSpots) {
        return availableSpots.get(0);
    }
}
