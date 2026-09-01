package com.ansh.exception;

import com.ansh.enums.ErrorCode;
import lombok.Getter;

@Getter
public class FitnessBookingException extends RuntimeException {
    private final ErrorCode errorCode;

    public FitnessBookingException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    public FitnessBookingException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }
}