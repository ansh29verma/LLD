package com.ansh.exception;

public class TaskAlreadyCompletedException extends TodoException {

    public TaskAlreadyCompletedException(String taskId) {

        super("Task is already completed: " + taskId);
    }
}
