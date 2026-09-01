package com.ansh.repo;

import com.ansh.entity.Booking;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryBookingRepository implements BookingRepository {
    private final Map<String, Booking> storage = new ConcurrentHashMap<>();

    @Override
    public Booking save(Booking booking) {
        storage.put(booking.getBookingId(), booking);
        return booking;
    }

    @Override
    public Optional<Booking> findById(String bookingId) {
        return Optional.ofNullable(storage.get(bookingId));
    }

    @Override
    public Optional<Booking> findByUserIdAndClassId(String userId, String classId) {
        return storage.values().stream()
                .filter(b -> b.getUserId().equals(userId) && b.getClassId().equals(classId))
                .findFirst();
    }

    @Override
    public List<Booking> findByUserId(String userId) {
        return storage.values().stream()
                .filter(b -> b.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}
