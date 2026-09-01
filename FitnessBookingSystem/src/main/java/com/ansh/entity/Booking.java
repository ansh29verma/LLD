package com.ansh.entity;


import com.ansh.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    private String bookingId;
    private String userId;
    private String classId;
    private BookingStatus status;
    private LocalDateTime bookedAt;
}