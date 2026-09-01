package com.ansh.repo;

import com.ansh.entity.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    Booking save(Booking booking);
    Optional<Booking> findById(String bookingId);
    Optional<Booking> findByUserIdAndClassId(String userId, String classId);
    List<Booking> findByUserId(String userId);
}
