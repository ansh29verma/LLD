package com.ansh.FareStrategy.ConcreteStrategies;

import com.ansh.FareStrategy.DurationType;
import com.ansh.FareStrategy.ParkingFeeStrategy;
import com.ansh.Ticket;
import com.ansh.VehicleFactory.VehicleType;

import java.time.temporal.ChronoUnit;

/**
 * Basic hourly rate strategy.
 * FIX: ChronoUnit.HOURS.between() arguments were reversed — (exitTime, entryTime)
 *      gives a negative duration. Corrected to (entryTime, exitTime).
 * FIX: Removed unused import PaymentStrategy.
 */
public class BasicHourlyRateStrategy implements ParkingFeeStrategy {


    private double calculateFee(VehicleType vehicleType, int duration, DurationType durationType) {
        switch (vehicleType) {
            case CAR:
                return durationType == DurationType.HOURS ? duration * 10.0 : duration * 10.0 * 24;
            case BIKE:
                return durationType == DurationType.HOURS ? duration * 5.0  : duration * 5.0  * 24;
            case OTHER:
                return durationType == DurationType.HOURS ? duration * 8.0  : duration * 8.0  * 24;
            default:
                return durationType == DurationType.HOURS ? duration * 15.0 : duration * 15.0 * 24;
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
