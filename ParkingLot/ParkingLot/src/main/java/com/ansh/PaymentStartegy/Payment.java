package com.ansh.PaymentStartegy;

import com.ansh.Ticket;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Payment receipt — immutable record of a completed transaction.
 *
 * FIX 1: Removed pType field — it was declared but never set (always null).
 *         The paymentStrategy reference already carries type information.
 *         PaymentType enum is still available if needed for future reporting.
 *
 * FIX 2: Removed processPayment() method — payment is already processed
 *         in ExitGate.makePayment() before this object is created.
 *         Having it here creates confusion about when/whether to call it again.
 *         Payment is a receipt, not an action trigger.
 */
@Getter
public class Payment {

    private final String          paymentId;
    private final LocalDateTime   pTime;
    private final Ticket          ticket;
    private final double          amount;
    private final PaymentStrategy paymentStrategy;

    public Payment(double amount, Ticket ticket, PaymentStrategy pStrategy) {
        this.paymentId       = UUID.randomUUID().toString();
        this.pTime           = LocalDateTime.now();
        this.amount          = amount;
        this.ticket          = ticket;
        this.paymentStrategy = pStrategy;
    }
}
