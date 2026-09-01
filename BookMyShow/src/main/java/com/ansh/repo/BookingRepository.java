package com.ansh.repo;

import com.ansh.entity.Booking;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BookingRepository {
    private final Map<String, Booking> db = new HashMap<>();

    public void save(Booking booking) {
        db.put(booking.getId(), booking);
    }

    public Booking get(String id) {
        return db.get(id);
    }

    public Collection<Booking> getAll() {
        return db.values();
    }
}
