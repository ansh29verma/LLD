package com.ansh.exceptions;

import com.ansh.enums.ErrorCode;
import lombok.Getter;

@Getter
public class CommunicationException extends RuntimeException {
    private final ErrorCode errorCode;

    public CommunicationException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    public CommunicationException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }
}
