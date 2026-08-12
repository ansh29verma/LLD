package com.ansh.VehicleFactory;

import com.ansh.VehicleFactory.Concrete.EconomyVehicle;
import com.ansh.VehicleFactory.Concrete.LuxuryVehicle;
import com.ansh.VehicleFactory.Concrete.SUVVehicle;

public class VehicleFactory {

    public static Vehicle createVehicle(VehicleType vehicleType, String registrationNumber, String model, double baseRentalPrice) {
        switch (vehicleType) {
            case ECONOMY:
                return new EconomyVehicle(registrationNumber, model, vehicleType,  baseRentalPrice);
            case LUXURY:
                return new LuxuryVehicle(registrationNumber, model, vehicleType, baseRentalPrice);
            case SUV:
                return new SUVVehicle(registrationNumber, model,vehicleType,  baseRentalPrice);
            default:
                throw new IllegalArgumentException("Unsupported vehicle type: " + vehicleType);
        }
    }
}
