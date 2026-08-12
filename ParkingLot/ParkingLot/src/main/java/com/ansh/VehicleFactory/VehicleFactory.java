package com.ansh.VehicleFactory;

import com.ansh.VehicleFactory.ConcreteVehicles.BikeVehicle;
import com.ansh.VehicleFactory.ConcreteVehicles.CarVehicle;
import com.ansh.VehicleFactory.ConcreteVehicles.OtherVehicle;

/**
 * Factory Pattern — creates correct Vehicle subtype.
 * FIX: Removed ParkingFeeStrategy parameter — fee strategy belongs to
 *      the parking lot/gate, not the vehicle.
 */
public class VehicleFactory {

    public static Vehicle createVehicle(VehicleType vehicleType, String licensePlate) {
        switch (vehicleType) {
            case CAR:   return new CarVehicle(licensePlate);
            case BIKE:  return new BikeVehicle(licensePlate);
            default:    return new OtherVehicle(licensePlate);
        }
    }
}
