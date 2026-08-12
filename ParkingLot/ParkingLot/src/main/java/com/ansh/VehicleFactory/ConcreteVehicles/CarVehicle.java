package com.ansh.VehicleFactory.ConcreteVehicles;

import com.ansh.VehicleFactory.Vehicle;
import com.ansh.VehicleFactory.VehicleType;

public class CarVehicle extends Vehicle {
    public CarVehicle(String licensePlate) {
        super(licensePlate, VehicleType.CAR);
    }
}
