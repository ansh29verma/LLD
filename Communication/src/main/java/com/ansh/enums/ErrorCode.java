package com.ansh.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    PROVIDER_NOT_FOUND("ERR_001", "Provider not found"),
    PROVIDER_ALREADY_EXISTS("ERR_002", "Provider already exists"),
    INVALID_PROVIDER_DATA("ERR_003", "Invalid provider details provided"),
    NO_ELIGIBLE_PROVIDER("ERR_004", "No active provider available for the requested channel and account"),
    INVALID_REQUEST("ERR_005", "Invalid communication request input"),
    PROVIDER_CALL_FAILED("ERR_006", "Failed to communicate with provider API");

    private final String code;
    private final String description;
}
