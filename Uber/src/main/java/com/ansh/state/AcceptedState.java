package com.ansh.state;

// File: com/uber/state/AcceptedState.java

import com.ansh.entity.Ride;
import com.ansh.exceptions.InvalidStateTransitionException;

public class AcceptedState implements RideState {
    @Override
    public void accept(Ride ride) {
        throw new InvalidStateTransitionException("Ride is already accepted.");
    }

    @Override
    public void start(Ride ride) {
        ride.setCurrentState(new InProgressState());
        System.out.println("[State Transition] Ride " + ride.getId() + " moved to IN_PROGRESS. Trip started.");
    }

    @Override
    public void complete(Ride ride) {
        throw new InvalidStateTransitionException("Ride cannot be completed before starting.");
    }

    @Override
    public void cancel(Ride ride) {
        ride.setCurrentState(new CancelledState());
        ride.getDriver().markAvailable();
        System.out.println("[State Transition] Ride " + ride.getId() + " cancelled. Driver released.");
    }

    @Override
    public String getStateName() {
        return "ACCEPTED";
    }
}
