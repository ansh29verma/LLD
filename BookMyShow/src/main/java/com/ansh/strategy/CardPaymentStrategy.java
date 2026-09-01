package com.ansh.strategy;

import com.ansh.entity.Booking;

class CardPaymentStrategy implements PaymentStrategy {
    @Override
    public boolean pay(Booking booking) {
        System.out.println("[Card Payment] Processing payment of INR " + booking.getAmount() + " for Booking ID: " + booking.getId());
        return true;
    }
}

