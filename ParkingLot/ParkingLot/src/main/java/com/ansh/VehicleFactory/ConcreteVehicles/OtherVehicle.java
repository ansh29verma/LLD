package com.ansh.VehicleFactory.ConcreteVehicles;

import com.ansh.VehicleFactory.Vehicle;
import com.ansh.VehicleFactory.VehicleType;

public class OtherVehicle extends Vehicle {
    public OtherVehicle(String licensePlate) {
        super(licensePlate, VehicleType.OTHER);
    }
}
