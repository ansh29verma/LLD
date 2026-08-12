package com.ansh.PaymentStartegy.ConcreteStrategy;

import com.ansh.PaymentStartegy.PaymentStrategy;

/**
 * FIX 1: Removed constructor parameter `double fee` — PaymentStrategy
 *         is stateless; the amount is passed at processPayment() call time.
 *         Having fee in constructor violates Strategy pattern intent.
 * FIX 2: Corrected print message from "credit card" to "cash".
 */
public class CashPayment implements PaymentStrategy {

    public CashPayment() {}

    @Override
    public void processPayment(double amount) {
        System.out.println("Processing cash payment of $" + amount);
    }
}
