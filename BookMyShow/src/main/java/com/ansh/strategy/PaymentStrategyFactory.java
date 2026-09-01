package com.ansh.strategy;

import com.ansh.enums.PaymentType;

public class PaymentStrategyFactory {
    public static PaymentStrategy getPaymentStrategy(PaymentType paymentType) {
        switch (paymentType) {
            case UPI:
                return new UPIPaymentStrategy();
            case CARD:
                return new CardPaymentStrategy();
            default:
                throw new IllegalArgumentException("Unknown payment type");
        }
    }
}
