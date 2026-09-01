package com.ansh.entity;

import com.ansh.enums.SeatType;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public abstract class Seat {
    private final String id;
    private final double price;

    public abstract SeatType getType();
}
