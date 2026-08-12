package com.ansh.ParkingSpots.ConcreteParkingSpots;

import com.ansh.ParkingSpots.ParkingSpot;
import com.ansh.ParkingSpots.ParkingSpotType;
import com.ansh.VehicleFactory.Vehicle;
import com.ansh.VehicleFactory.VehicleType;

public class CarParkingSpot extends ParkingSpot {

    public CarParkingSpot(int spotNumber) {
        super(spotNumber, ParkingSpotType.CarParking);
    }

    @Override
    public boolean canParkVehicle(Vehicle vehicle) {
        return vehicle.getVehicleType() == VehicleType.CAR;
    }
}
