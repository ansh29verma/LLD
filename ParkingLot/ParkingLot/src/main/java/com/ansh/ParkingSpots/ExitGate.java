package com.ansh.ParkingSpots;

import com.ansh.FareStrategy.ParkingFeeStrategy;
import com.ansh.PaymentStartegy.Payment;
import com.ansh.PaymentStartegy.PaymentStrategy;
import com.ansh.Ticket;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExitGate {
    String gateId;
    private final ParkingFeeStrategy feeStrategy;

    /**
     * FIX: Removed paymentStrategy from constructor.
     * Payment strategy must be chosen by the user at exit time (per vehicle),
     * not fixed at gate construction time. It is now passed per makePayment() call.
     */
    public ExitGate(String gateId, ParkingFeeStrategy feeStrategy) {
        this.gateId = gateId;
        this.feeStrategy = feeStrategy;
    }

    /**
     * Processes exit for a ticket using the payment strategy selected by the user.
     *
     * @param ticket          the parking ticket to close out
     * @param paymentStrategy chosen by the user at exit time
     */
    public Payment makePayment(Ticket ticket, PaymentStrategy paymentStrategy) {
        ticket.setExitTime(LocalDateTime.now());
        double charges = feeStrategy.calculateFee(ticket);
        ticket.setCharges(charges);
        paymentStrategy.processPayment(charges);
        ticket.setActive(false);
        freeParkingSpace(ticket.getPSpaceAssigned());
        return new Payment(charges, ticket, paymentStrategy);
    }

    private void freeParkingSpace(ParkingSpot pSpace) {
        pSpace.vacate();
    }
}
