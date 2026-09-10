package com.ansh.state;

// File: com/uber/state/InProgressState.java


import com.ansh.entity.Ride;
import com.ansh.exceptions.InvalidStateTransitionException;

public class InProgressState implements RideState {
    @Override
    public void accept(Ride ride) {
        throw new InvalidStateTransitionException("Ride is already ongoing.");
    }

    @Override
    public void start(Ride ride) {
        throw new InvalidStateTransitionException("Ride has already started.");
    }

    @Override
    public void complete(Ride ride) {
        ride.setCurrentState(new CompletedState());
        ride.getDriver().markAvailable();
        System.out.println("[State Transition] Ride " + ride.getId() + " moved to COMPLETED. Driver marked available.");
    }

    @Override
    public void cancel(Ride ride) {
        throw new InvalidStateTransitionException("Cannot cancel an in-progress ride directly without driver intervention.");
    }

    @Override
    public String getStateName() {
        return "IN_PROGRESS";
    }
}