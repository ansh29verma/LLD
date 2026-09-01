package com.ansh.entity;

import com.ansh.enums.SeatType;

public class ReclinerSeat extends Seat {
    public ReclinerSeat(String id) {
        super(id, 300.0);
    }

    @Override
    public SeatType getType() {
        return SeatType.RECLINER;
    }
}
