package com.ansh;

import com.ansh.ParkingSpots.ParkingSpot;
import com.ansh.VehicleFactory.Vehicle;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Ticket {

    private final String ticketId;
    private final LocalDateTime entryTime;
    private final Vehicle vehicle;
    private final ParkingSpot pSpaceAssigned;
    @Setter  private LocalDateTime exitTime;
    @Setter private boolean isActive;
    @Setter double charges;

    public Ticket(Vehicle v, ParkingSpot pSpace)
    {
        this.ticketId= UUID.randomUUID().toString();
        this.entryTime= LocalDateTime.now();
        this.isActive= true;
        this.vehicle= v;
        this.pSpaceAssigned= pSpace;
    }

}
