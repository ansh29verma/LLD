package com.ansh.entity;



import lombok.Getter;
import lombok.Setter;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class Driver {
    private final String id;
    private final String name;
    @Setter private Location currentLocation;
    private final Vehicle vehicle;
    private final double rating;
    private final AtomicBoolean available = new AtomicBoolean(true);

    public Driver(String id, String name, Location currentLocation, Vehicle vehicle, double rating) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        this.vehicle = vehicle;
        this.rating = rating;
    }

    public boolean isAvailable() {
        return available.get();
    }

    public boolean markUnavailable() {
        return available.compareAndSet(true, false);
    }

    public void markAvailable() {
        available.set(true);
    }
}
