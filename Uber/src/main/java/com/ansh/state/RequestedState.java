package com.ansh.state;

import com.ansh.entity.Ride;
import com.ansh.exceptions.InvalidStateTransitionException;

public class RequestedState implements RideState {
    @Override
    public void accept(Ride ride) {
        ride.setCurrentState(new AcceptedState());
        System.out.println("[State Transition] Ride " + ride.getId() + " moved to ACCEPTED.");
    }

    @Override
    public void start(Ride ride) {
        throw new InvalidStateTransitionException("Cannot start a ride before it is accepted by a driver.");
    }

    @Override
    public void complete(Ride ride) {
        throw new InvalidStateTransitionException("Cannot complete a ride that has not started.");
    }

    @Override
    public void cancel(Ride ride) {
        ride.setCurrentState(new CancelledState());
        System.out.println("[State Transition] Ride " + ride.getId() + " cancelled by rider.");
    }

    @Override
    public String getStateName() {
        return "REQUESTED";
    }
}