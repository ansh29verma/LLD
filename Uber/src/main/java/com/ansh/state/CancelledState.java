package com.ansh.state;


import com.ansh.entity.Ride;
import com.ansh.exceptions.InvalidStateTransitionException;

public class CancelledState implements RideState {
    @Override public void accept(Ride ride) { throw new InvalidStateTransitionException("Terminal state reached."); }
    @Override public void start(Ride ride) { throw new InvalidStateTransitionException("Terminal state reached."); }
    @Override public void complete(Ride ride) { throw new InvalidStateTransitionException("Terminal state reached."); }
    @Override public void cancel(Ride ride) { throw new InvalidStateTransitionException("Terminal state reached."); }
    @Override public String getStateName() { return "CANCELLED"; }
}
