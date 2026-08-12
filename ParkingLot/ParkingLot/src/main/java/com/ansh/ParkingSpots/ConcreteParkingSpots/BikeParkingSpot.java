package com.ansh.ParkingSpots.ConcreteParkingSpots;

import com.ansh.ParkingSpots.ParkingSpot;
import com.ansh.ParkingSpots.ParkingSpotType;
import com.ansh.VehicleFactory.ConcreteVehicles.BikeVehicle;
import com.ansh.VehicleFactory.Vehicle;
import com.ansh.VehicleFactory.VehicleType;

public class BikeParkingSpot extends ParkingSpot {

    public BikeParkingSpot(int spotNumber) {
        super(spotNumber, ParkingSpotType.BikeParking);
    }

    @Override
    public boolean canParkVehicle(Vehicle vehicle) {
        return vehicle.getVehicleType()== VehicleType.BIKE;

    }

}
