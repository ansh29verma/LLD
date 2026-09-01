package com.ansh.service;

import com.ansh.dto.FitnessClassResponseDto;
import com.ansh.entity.Booking;
import com.ansh.entity.User;
import com.ansh.enums.ClassType;
import com.ansh.enums.UserTier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingService {
    User registerUser(String userId, String name, String email, UserTier tier);

    FitnessClassResponseDto createClass(String classId, String title, ClassType classType, int capacity, LocalDateTime startTime, LocalDateTime endTime);

    void cancelClassByAdmin(String classId);

    Booking bookClass(String userId, String classId);

    void cancelBooking(String userId, String classId, LocalDateTime currentTime);

    List<FitnessClassResponseDto> getAllClasses();

    List<Booking> getUserBookings(String userId);
}