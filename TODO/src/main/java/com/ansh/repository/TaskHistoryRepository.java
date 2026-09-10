package com.ansh.repository;

import com.ansh.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskHistoryRepository {
    void save(Task task);
    Optional<Task> findById(String taskId);
    List<Task> findAll();
}
