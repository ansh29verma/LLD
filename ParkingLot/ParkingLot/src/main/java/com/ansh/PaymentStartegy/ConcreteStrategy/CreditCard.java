package com.ansh.PaymentStartegy.ConcreteStrategy;

import com.ansh.PaymentStartegy.PaymentStrategy;

/**
 * FIX: Removed constructor parameter `double fee` — same reason as CashPayment.
 *      Strategy is stateless; amount is passed per-call to processPayment().
 */
public class CreditCard implements PaymentStrategy {

    public CreditCard() {}

    @Override
    public void processPayment(double amount) {
        System.out.println("Processing credit card payment of $" + amount);
    }
}
