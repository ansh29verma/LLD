package com.ansh.exceptions;


import com.ansh.enums.ErrorCode;
import lombok.Getter;

@Getter
public class ResolutionSystemException extends RuntimeException {
    private final ErrorCode errorCode;

    public ResolutionSystemException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    public ResolutionSystemException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }
}