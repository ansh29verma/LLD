package com.ansh.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    APP_NOT_FOUND("ERR_001", "App not found"),
    VERSION_NOT_FOUND("ERR_002", "App version not found"),
    DEVICE_NOT_FOUND("ERR_003", "Device not found"),
    INVALID_VERSION("ERR_004", "Invalid version number format"),
    UNSUPPORTED_DEVICE("ERR_005", "Device hardware or OS version is not supported"),
    PATCH_CREATION_FAILED("ERR_006", "Failed to create update patch"),
    NO_UPDATE_AVAILABLE("ERR_007", "App is already up to date"),
    EXECUTION_FAILED("ERR_008", "Execution of install/update task failed");

    private final String code;
    private final String description;
}