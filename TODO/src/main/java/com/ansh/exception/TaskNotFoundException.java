package com.ansh.exception;

public class TaskNotFoundException extends TodoException {

    public TaskNotFoundException(String taskId) {

        super("Task not found: " + taskId);
    }
}