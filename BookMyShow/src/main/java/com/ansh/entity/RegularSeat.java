package com.ansh.entity;

import com.ansh.enums.SeatType;

public class RegularSeat extends Seat {
    public RegularSeat(String id) {
        super(id, 150.0);
    }

    @Override
    public SeatType getType() {
        return SeatType.REGULAR;
    }
}