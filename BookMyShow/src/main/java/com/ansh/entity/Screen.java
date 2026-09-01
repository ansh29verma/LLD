package com.ansh.entity;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class Screen {
    private final String id;
    private final Map<String, Seat> seats = new HashMap<>();


    public void addSeat(Seat seat) {
        seats.put(seat.getId(), seat);
    }

    public Seat getSeat(String id) {
        return seats.get(id);
    }


}