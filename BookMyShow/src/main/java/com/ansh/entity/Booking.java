package com.ansh.entity;

import com.ansh.enums.BookingStatus;
import com.ansh.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Booking {
    private final String id;
    private final String userId;
    private final String showId;
    private final List<Seat> seats;
    private BookingStatus status;
    private PaymentType paymentType;
    private final double amount;
}
