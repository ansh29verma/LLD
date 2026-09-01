package com.ansh.exception;




import com.ansh.enums.ErrorCode;
import lombok.Getter;

@Getter
public class HackathonException extends RuntimeException {
    private final ErrorCode errorCode;

    public HackathonException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    public HackathonException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }
}