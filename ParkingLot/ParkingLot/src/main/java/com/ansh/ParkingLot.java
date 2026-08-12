package com.ansh;

import com.ansh.ParkingSpots.EntryGate;
import com.ansh.ParkingSpots.ExitGate;
import com.ansh.ParkingSpots.Floor;
import com.ansh.ParkingSpots.ParkingSpot;
import com.ansh.ParkingStartegy.NaturalOrderParking;
import com.ansh.ParkingStartegy.ParkingStrategy;
import com.ansh.VehicleFactory.Vehicle;
import com.ansh.VehicleFactory.VehicleType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Singleton Pattern — thread-safe double-checked locking.
 *
 * FIX: ParkingStrategy was stored but findAvailableSpot() never used it.
 *      Now findAvailableSpot() collects all available spots per floor and
 *      delegates the selection to parkingStrategy.park().
 *      Default strategy: NaturalOrderParking (first available spot).
 */
@Getter
public class ParkingLot {

    private final String pLotId;
    private final List<Floor> floors;
    private final List<EntryGate> entries;
    private final List<ExitGate> exits;
    private ParkingStrategy parkingStrategy;

    private static volatile ParkingLot INSTANCE;

    private ParkingLot() {
        pLotId          = UUID.randomUUID().toString();
        floors          = new ArrayList<>();
        entries         = new ArrayList<>();
        exits           = new ArrayList<>();
        parkingStrategy = new NaturalOrderParking(); // default strategy
    }

    public static ParkingLot getInstance() {
        if (INSTANCE == null) {
            synchronized (ParkingLot.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ParkingLot();
                }
            }
        }
        return INSTANCE;
    }

    public void addFloor(Floor floor)             { floors.add(floor); }
    public void addEntryGate(EntryGate g)         { entries.add(g); }
    public void addExitGate(ExitGate g)           { exits.add(g); }
    public void setParkingStrategy(ParkingStrategy s) { this.parkingStrategy = s; }

    /**
     * FIX: Now collects all available spots per floor and passes them to
     *      parkingStrategy.park() — ParkingStrategy is finally integrated.
     */
    public ParkingSpot findAvailableSpot(VehicleType vehicleType) {
        for (Floor floor : floors) {
            List<ParkingSpot> available = floor.getAvailableSpots(vehicleType);
            if (!available.isEmpty()) {
                return parkingStrategy.park(available);
            }
        }
        return null;
    }

    public ParkingSpot parkVehicle(Vehicle vehicle) {
        ParkingSpot spot = findAvailableSpot(vehicle.getVehicleType());
        if (spot != null) {
            spot.parkVehicle(vehicle);
            System.out.println("Vehicle parked in spot: " + spot.getSpotNumber());
            return spot;
        }
        System.out.println("No parking spot available for: " + vehicle.getVehicleType());
        return null;
    }

    public void vacateSpot(ParkingSpot spot, Vehicle vehicle) {
        if (spot != null && spot.isOccupied()
                && spot.getVehicle().equals(vehicle)) {
            spot.vacate();
            System.out.println(vehicle.getVehicleType()
                    + " vacated spot: " + spot.getSpotNumber());
        } else {
            System.out.println("Invalid: spot is vacant or vehicle mismatch.");
        }
    }

    public ParkingSpot getSpotByNumber(int spotNumber) {
        for (Floor floor : floors) {
            for (ParkingSpot spot : floor.getParkingSpots()) {
                if (spot.getSpotNumber() == spotNumber) return spot;
            }
        }
        return null;
    }
}
