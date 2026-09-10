package com.ansh.service;

import com.ansh.entity.*;
import com.ansh.enums.ActivityType;
import com.ansh.enums.TaskStatus;
import com.ansh.exception.*;
import com.ansh.repository.ActivityRepository;
import com.ansh.repository.TaskHistoryRepository;
import com.ansh.repository.TaskRepository;
import com.ansh.strategy.TaskSortStrategy;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class TodoServiceImpl implements TodoService {

    private final TaskRepository taskRepository;
    private final TaskHistoryRepository historyRepository;
    private final ActivityRepository activityRepository;
    private final TaskSortStrategy sortStrategy;

    public TodoServiceImpl(TaskRepository taskRepository, TaskHistoryRepository historyRepository, ActivityRepository activityRepository, TaskSortStrategy sortStrategy) {
        this.taskRepository = taskRepository;
        this.historyRepository = historyRepository;
        this.activityRepository = activityRepository;
        this.sortStrategy = sortStrategy;
    }

    @Override
    public void addTask(Task task) {
        validateTask(task);

        if (taskRepository.findById(task.getId()).isPresent()) {
            throw new InvalidTaskException(
                    "Task already exists: " + task.getId()
            );
        }

        LocalDateTime now = LocalDateTime.now();

        task.setStatus(TaskStatus.PENDING);
        task.setUpdatedAt(now);

        taskRepository.save(task);
        historyRepository.save(task);
        addActivity(task, ActivityType.ADDED);
    }

    @Override
    public Task getTask(String taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    @Override
    public void modifyTask(Task task) {
        validateTask(task);

        Task existingTask = getTask(task.getId());

        if (existingTask.getStatus() != TaskStatus.PENDING) {
            throw new InvalidTaskException(
                    "Only pending tasks can be modified"
            );
        }

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setDeadline(task.getDeadline());
        existingTask.setScheduledAt(task.getScheduledAt());
        existingTask.setTags(task.getTags());
        existingTask.setUpdatedAt(LocalDateTime.now());

        taskRepository.save(existingTask);
        historyRepository.save(existingTask);

        addActivity(existingTask, ActivityType.MODIFIED);
    }

    @Override
    public void removeTask(String taskId) {
        Task task = getTask(taskId);

        if (task.getStatus() != TaskStatus.PENDING) {
            throw new InvalidTaskException(
                    "Only pending tasks can be removed"
            );
        }
        task.setStatus(TaskStatus.REMOVED);
        task.setUpdatedAt(LocalDateTime.now());
        historyRepository.save(task);
        addActivity(task, ActivityType.REMOVED);
        taskRepository.delete(taskId);
    }

    @Override
    public void completeTask(String taskId) {
        Task task = getTask(taskId);

        if (task.getStatus() == TaskStatus.COMPLETED) {
            throw new TaskAlreadyCompletedException(taskId);
        }

        if (task.getStatus() != TaskStatus.PENDING) {
            throw new InvalidTaskException(
                    "Only pending tasks can be completed"
            );
        }

        LocalDateTime now = LocalDateTime.now();

        task.setStatus(TaskStatus.COMPLETED);
        task.setCompletedAt(now);
        task.setUpdatedAt(now);

        historyRepository.save(task);
        addActivity(task, ActivityType.COMPLETED);
        taskRepository.delete(taskId);
    }

    @Override
    public List<Task> listTasks(TaskFilter filter) {

        if (filter == null) {
            throw new InvalidTaskException("Filter cannot be null");
        }

        LocalDateTime now = LocalDateTime.now();

        return taskRepository.findAll()
                .stream()
                .filter(task -> task.getStatus() == TaskStatus.PENDING)
                .filter(task -> filter.getUserId() == null
                        || task.getUserId().equals(filter.getUserId()))
                .filter(task -> filter.isIncludeFutureTasks()
                        || task.getScheduledAt() == null
                        || !task.getScheduledAt().isAfter(now))
                .filter(task -> filter.getTags() == null
                        || filter.getTags().isEmpty()
                        || task.getTags().containsAll(filter.getTags()))
                .filter(task -> filter.getDeadlineAfter() == null
                        || task.getDeadline() == null
                        || !task.getDeadline()
                        .isBefore(filter.getDeadlineAfter()))
                .filter(task -> filter.getDeadlineBefore() == null
                        || task.getDeadline() == null
                        || !task.getDeadline()
                        .isAfter(filter.getDeadlineBefore()))
                .filter(task -> filter.getScheduledAfter() == null
                        || task.getScheduledAt() == null
                        || !task.getScheduledAt()
                        .isBefore(filter.getScheduledAfter()))
                .filter(task -> filter.getScheduledBefore() == null
                        || task.getScheduledAt() == null
                        || !task.getScheduledAt()
                        .isAfter(filter.getScheduledBefore()))
                .sorted(sortStrategy.getComparator())
                .collect(Collectors.toList());
    }

    @Override
    public Statistics getStatistics(TimePeriod period) {

        List<Activity> activities = activityRepository.findAll()
                .stream()
                .filter(activity ->
                        period == null
                                || period.contains(activity.getTimestamp()))
                .collect(Collectors.toList());

        long tasksAdded = activities.stream()
                .filter(activity -> activity.getType() == ActivityType.ADDED)
                .count();

        long tasksCompleted = activities.stream()
                .filter(activity -> activity.getType() == ActivityType.COMPLETED)
                .count();

        long tasksSpilledOverDeadline = activities.stream()
                .filter(activity -> activity.getType() == ActivityType.COMPLETED)
                .filter(this::completedAfterDeadline)
                .count();

        return Statistics.builder()
                .tasksAdded(tasksAdded)
                .tasksCompleted(tasksCompleted)
                .tasksSpilledOverDeadline(tasksSpilledOverDeadline)
                .build();
    }

    @Override
    public List<Activity> getActivityLog(TimePeriod period) {

        return activityRepository.findAll()
                .stream()
                .filter(activity ->
                        period == null
                                || period.contains(activity.getTimestamp()))
                .sorted(
                        Comparator.comparing(Activity::getTimestamp)
                )
                .collect(Collectors.toList());
    }

    private boolean completedAfterDeadline(Activity activity) {

        Task task = historyRepository.findById(activity.getTaskId()).orElse(null);

        return task != null && task.getDeadline() != null && activity.getTimestamp().isAfter(task.getDeadline());
    }

    private void addActivity(Task task, ActivityType type) {

        Activity activity = Activity.builder()
                .taskId(task.getId())
                .userId(task.getUserId())
                .type(type)
                .timestamp(LocalDateTime.now())
                .build();

        activityRepository.save(activity);
    }

    private void validateTask(Task task) {

        if (task == null) {
            throw new InvalidTaskException("Task cannot be null");
        }

        if (task.getId() == null || task.getId().isBlank()) {
            throw new InvalidTaskException("Task ID is required");
        }

        if (task.getUserId() == null || task.getUserId().isBlank()) {
            throw new InvalidTaskException("User ID is required");
        }

        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new InvalidTaskException("Task title is required");
        }

        if (task.getDeadline() != null && task.getScheduledAt() != null && task.getDeadline().isBefore(task.getScheduledAt())) {
            throw new InvalidTaskException(
                    "Deadline cannot be before scheduled time"
            );
        }
    }
}