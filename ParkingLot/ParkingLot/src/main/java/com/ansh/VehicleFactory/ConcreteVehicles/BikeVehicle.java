package com.ansh.VehicleFactory.ConcreteVehicles;

import com.ansh.VehicleFactory.Vehicle;
import com.ansh.VehicleFactory.VehicleType;

public class BikeVehicle extends Vehicle {
    public BikeVehicle(String licensePlate) {
        super(licensePlate, VehicleType.BIKE);
    }
}
