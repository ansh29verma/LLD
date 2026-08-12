package com.ansh.PaymentStrategy.ConcretePaymentStrategies;

import com.ansh.PaymentStrategy.PaymentStrategy;

import javax.smartcardio.Card;

public class CreditCard implements PaymentStrategy {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing credit card payment of $" + amount);
    }
}
