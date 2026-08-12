package com.ansh.ParkingStartegy;

import com.ansh.ParkingSpots.ParkingSpot;

import java.util.List;

/**
 * FIX: Changed from abstract class to interface.
 * Single abstract method + no state = interface, not abstract class.
 * Allows concrete strategies to extend other classes if needed.
 */
public interface ParkingStrategy {
    ParkingSpot park(List<ParkingSpot> availableSpots);
}
