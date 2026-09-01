package com.ansh.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND("ERR_001", "User not found"),
    CLASS_NOT_FOUND("ERR_002", "Fitness class not found"),
    USER_ALREADY_EXISTS("ERR_003", "User already registered"),
    CLASS_ALREADY_EXISTS("ERR_004", "Class ID already exists"),
    BOOKING_LIMIT_EXCEEDED("ERR_005", "User booking quota limit exceeded for tier"),
    ALREADY_BOOKED_OR_WAITLISTED("ERR_006", "User already booked or waitlisted for this class"),
    CANCELLATION_WINDOW_EXPIRED("ERR_007", "Cancellation window expired (must cancel at least 30 minutes before start time)"),
    BOOKING_NOT_FOUND("ERR_008", "No active booking found for this user and class"),
    CLASS_ALREADY_CANCELLED("ERR_009", "Fitness class is already cancelled"),
    INVALID_OPERATION("ERR_010", "Invalid operation requested");

    private final String code;
    private final String description;
}
