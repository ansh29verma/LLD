package com.ansh.exceptions;

public class NoDriverAvailableException extends RuntimeException {
    public NoDriverAvailableException(String message) {
        super(message);
    }
}
