package com.ansh.FareStrategy.ConcreteStrategies;

import com.ansh.FareStrategy.DurationType;
import com.ansh.FareStrategy.ParkingFeeStrategy;
import com.ansh.Ticket;
import com.ansh.VehicleFactory.VehicleType;

import java.time.temporal.ChronoUnit;

/**
 * Premium rate strategy — 1.5x the basic rates.
 * FIX 1: Rates were identical to BasicHourlyRateStrategy — now correctly
 *         set to premium (1.5x multiplier).
 * FIX 2: ChronoUnit.HOURS.between() arguments were reversed — corrected.
 * FIX 3: Removed unused import PaymentStrategy.
 * FIX 4: Refactored calculateFee(Ticket) to delegate to calculateFee(VehicleType,...)
 *         to avoid duplicated switch logic.
 */
public class PremiumRateStrategy implements ParkingFeeStrategy {


    private double calculateFee(VehicleType vehicleType, int duration, DurationType durationType) {
        switch (vehicleType) {
            case CAR:
                return durationType == DurationType.HOURS ? duration * 15.0 : duration * 15.0 * 24;
            case BIKE:
                return durationType == DurationType.HOURS ? duration * 7.5  : duration * 7.5  * 24;
            case OTHER:
                return durationType == DurationType.HOURS ? duration * 12.0 : duration * 12.0 * 24;
            default:
                return durationType == DurationType.HOURS ? duration * 20.0 : duration * 20.0 * 24;
        }
    }

    @Override
    public double calculateFee(Ticket ticket) {
        VehicleType vehicleType = ticket.getVehicle().getVehicleType();
        // FIX: was between(exitTime, entryTime) — reversed, giving negative duration
        long duration = ChronoUnit.HOURS.between(ticket.getEntryTime(), ticket.getExitTime());
        return calculateFee(vehicleType, (int) duration, DurationType.HOURS);
    }
}
