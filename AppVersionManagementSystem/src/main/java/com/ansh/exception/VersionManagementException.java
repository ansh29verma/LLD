package com.ansh.exception;

import com.ansh.enums.ErrorCode;
import lombok.Getter;

@Getter
public class VersionManagementException extends RuntimeException {
    private final ErrorCode errorCode;

    public VersionManagementException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    public VersionManagementException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }
}
