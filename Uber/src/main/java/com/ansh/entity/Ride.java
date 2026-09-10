package com.ansh.entity;

// File: com/uber/model/Ride.java

import com.ansh.state.AcceptedState;
import com.ansh.state.RideState;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Ride {
    private final String id;
    private final Product product;
    private final Location source;
    private final Location destination;
    private final Rider rider;
    private final Driver driver;
    private final Vehicle vehicle;
    private final double fareAmount;

    @Setter
    private RideState currentState;

    public Ride(String id, Product product, Location source, Location destination,
                Rider rider, Driver driver, Vehicle vehicle, double fareAmount) {
        this.id = id;
        this.product = product;
        this.source = source;
        this.destination = destination;
        this.rider = rider;
        this.driver = driver;
        this.vehicle = vehicle;
        this.fareAmount = fareAmount;
        // Upon initialization from a successful match, the ride starts in ACCEPTED
        this.currentState = new AcceptedState();
    }

    public void start() {
        currentState.start(this);
    }

    public void complete() {
        currentState.complete(this);
    }

    public void cancel() {
        currentState.cancel(this);
    }

    public String getStatus() {
        return currentState.getStateName();
    }
}