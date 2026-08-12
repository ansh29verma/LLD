package com.ansh.ParkingSpots;

import com.ansh.ParkingLot;
import com.ansh.Ticket;
import com.ansh.VehicleFactory.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EntryGate {
    private String gateId;

    /**
     * Generates a parking ticket for the vehicle.
     *
     * FIX: Was calling findAvailableSpot() twice — once to check availability
     *      and again to get the spot. This is wasteful and introduces a race
     *      condition (spot could be taken between the two calls).
     *      Now calls findAvailableSpot() once, null-checks, then parks.
     * FIX: Updated ParkingLot.INSTANCE references to ParkingLot.getInstance()
     *      to align with the corrected thread-safe Singleton.
     */
    public Ticket generateTicket(Vehicle v) {
        ParkingSpot pSpace = ParkingLot.getInstance().findAvailableSpot(v.getVehicleType());
        if (pSpace == null) return null;

        pSpace.parkVehicle(v);
        System.out.println("Ticket generated — Vehicle: " + v.getLicensePlate()
                + " | Spot: " + pSpace.getSpotNumber());
        return new Ticket(v, pSpace);
    }
}
