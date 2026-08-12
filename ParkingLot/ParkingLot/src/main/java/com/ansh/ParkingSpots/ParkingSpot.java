package com.ansh.ParkingSpots;


import com.ansh.VehicleFactory.Vehicle;
import lombok.Data;

@Data
public abstract class ParkingSpot {
    private int spotNumber;
    private boolean isOccupied;
    private Vehicle vehicle;
    private ParkingSpotType spotType;


    public ParkingSpot(int spotNumber, ParkingSpotType spotType) {
        this.spotNumber = spotNumber;
        this.isOccupied = false;
        this.spotType = spotType;
    }

    public boolean isOccupied() {
        return isOccupied;
    }



    public abstract boolean canParkVehicle(Vehicle vehicle);
    // Method to park a vehicle in the spot
    public void parkVehicle(Vehicle vehicle) {
        // Check if the spot is already occupied
        if (isOccupied) {
            throw new IllegalStateException("Spot is already occupied.");
        }
        // Check if the vehicle can be parked in this spot
        if (!canParkVehicle(vehicle)) {
            throw new IllegalArgumentException(
                    "This spot is not suitable for" + vehicle.getVehicleType());
        }
        this.vehicle = vehicle;
        this.isOccupied = true;
    }
    // Method to vacate the parking spot
    public void vacate() {
        // Check if the spot is already vacant
        if (!isOccupied) {
            throw new IllegalStateException("Spot is already vacant.");
        }
        this.vehicle = null;
        this.isOccupied = false;
    }

}
