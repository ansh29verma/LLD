package com.ansh.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND("ERR_001", "User not found"),
    PROBLEM_NOT_FOUND("ERR_002", "Problem not found"),
    USER_ALREADY_EXISTS("ERR_003", "User email already exists"),
    PROBLEM_ALREADY_EXISTS("ERR_004", "Problem ID already exists"),
    PROBLEM_ALREADY_SOLVED("ERR_005", "Problem has already been solved by user"),
    INVALID_INPUT("ERR_006", "Invalid input parameters provided");

    private final String code;
    private final String description;
}