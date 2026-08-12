package com.ansh.ParkingSpots;

import com.ansh.VehicleFactory.VehicleType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * FIX: Removed unused imports — HashMap, Map, Vehicle were imported but never used.
 * FIX: Added getAvailableSpots(VehicleType) so ParkingLot can pass the list
 *      to ParkingStrategy.park() instead of finding just the first spot itself.
 */
@Data
public class Floor {
    private final String floorId;
    private List<ParkingSpot> spots;

    public Floor(String floorId) {
        this.floorId = floorId;
        this.spots   = new ArrayList<>();
    }

    public void addParkingSpot(ParkingSpot p)    { spots.add(p); }
    public void removeParkingSpot(ParkingSpot p) { spots.remove(p); }

    private ParkingSpotType getSpaceTypeForVehicle(VehicleType vType) {
        switch (vType) {
            case CAR:   return ParkingSpotType.CarParking;
            case BIKE:  return ParkingSpotType.BikeParking;
            case OTHER: return ParkingSpotType.OtherParking;
            default:    return null;
        }
    }

    /** Returns all unoccupied spots matching the vehicle type on this floor. */
    public List<ParkingSpot> getAvailableSpots(VehicleType vehicleType) {
        List<ParkingSpot> available = new ArrayList<>();
        ParkingSpotType required = getSpaceTypeForVehicle(vehicleType);
        for (ParkingSpot spot : spots) {
            if (!spot.isOccupied() && spot.getSpotType().equals(required)) {
                available.add(spot);
            }
        }
        return available;
    }

    /** Convenience: returns the first available spot (legacy use). */
    public ParkingSpot findAvailableSpot(VehicleType vehicleType) {
        List<ParkingSpot> available = getAvailableSpots(vehicleType);
        return available.isEmpty() ? null : available.get(0);
    }

    public List<ParkingSpot> getParkingSpots() { return spots; }
}
