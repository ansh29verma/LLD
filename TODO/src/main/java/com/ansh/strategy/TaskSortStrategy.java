package com.ansh.strategy;

import com.ansh.entity.Task;

import java.util.Comparator;

public interface TaskSortStrategy {
    Comparator<Task> getComparator();
}
