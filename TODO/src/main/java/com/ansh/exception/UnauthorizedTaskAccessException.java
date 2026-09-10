package com.ansh.exception;

public class UnauthorizedTaskAccessException extends TodoException {

    public UnauthorizedTaskAccessException(String taskId) {

        super("User does not have access to task: " + taskId);
    }
}
