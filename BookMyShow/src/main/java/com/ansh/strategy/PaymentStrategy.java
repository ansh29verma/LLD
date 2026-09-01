package com.ansh.strategy;

import com.ansh.entity.Booking;

public interface PaymentStrategy {
    boolean pay(Booking booking);
}
