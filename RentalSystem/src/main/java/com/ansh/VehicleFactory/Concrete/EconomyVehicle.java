package com.ansh.VehicleFactory.Concrete;

import com.ansh.VehicleFactory.Vehicle;
import com.ansh.VehicleFactory.VehicleType;

public class EconomyVehicle extends Vehicle {
    private static final double RATE_MULTIPLIER = 1.0;

    public EconomyVehicle(String registrationNumber, String model, VehicleType vehicleType, double baseRentalPrice) {
        super(registrationNumber,model,vehicleType,baseRentalPrice);
    }

    @Override
    public double calculateRentalPrice(int daysRented) {
        return getBaseRentalPrice() * RATE_MULTIPLIER*daysRented;
    }

}
