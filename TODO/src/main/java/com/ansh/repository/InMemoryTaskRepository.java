package com.ansh.repository;

import com.ansh.entity.Task;
import java.util.*;


public class InMemoryTaskRepository implements TaskRepository {

    private final Map<String, Task> tasks = new HashMap<>();

    @Override
    public void save(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public Optional<Task> findById(String taskId) {
        return Optional.ofNullable(
                tasks.get(taskId)
        );
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void delete(String taskId) {
        tasks.remove(taskId);
    }
}
