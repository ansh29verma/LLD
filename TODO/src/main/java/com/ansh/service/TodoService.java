package com.ansh.service;

import com.ansh.entity.*;

import java.util.List;

public interface TodoService {
    void addTask(Task task);

    Task getTask(String taskId);

    void modifyTask(Task task);

    void removeTask(String taskId);

    void completeTask(String taskId);

    List<Task> listTasks(TaskFilter filter);

    Statistics getStatistics(TimePeriod period);

    List<Activity> getActivityLog(TimePeriod period);
}