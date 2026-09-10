package com.ansh.state;

import com.ansh.entity.Ride;

public interface RideState {
    void accept(Ride ride);
    void start(Ride ride);
    void complete(Ride ride);
    void cancel(Ride ride);
    String getStateName();
}
