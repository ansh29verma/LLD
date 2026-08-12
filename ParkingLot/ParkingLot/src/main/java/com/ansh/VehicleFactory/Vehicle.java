package com.ansh.VehicleFactory;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * FIX: Removed ParkingFeeStrategy from Vehicle.
 *
 * A vehicle does NOT determine its own parking price.
 * Fee policy belongs to the parking lot operator (ExitGate).
 * Having feeStrategy on Vehicle was dead code — ExitGate.makePayment()
 * always used its own feeStrategy, never the vehicle's.
 */
@Data
@AllArgsConstructor
public class Vehicle {
    private String      licensePlate;
    private VehicleType vehicleType;
}
