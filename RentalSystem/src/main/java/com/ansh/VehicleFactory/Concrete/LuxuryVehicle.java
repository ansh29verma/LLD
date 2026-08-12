package com.ansh.VehicleFactory.Concrete;

import com.ansh.VehicleFactory.Vehicle;
import com.ansh.VehicleFactory.VehicleType;

public class LuxuryVehicle extends Vehicle {
    private static final double RATE_MULTIPLIER = 2.0;
    public LuxuryVehicle(String registrationNumber, String model, VehicleType vehicleType, double baseRentalPrice) {
        super(registrationNumber,model,vehicleType,baseRentalPrice);
    }

    @Override
    public double calculateRentalPrice(int daysRented) {
        return getBaseRentalPrice() * RATE_MULTIPLIER*daysRented;
    }
}
