package com.ansh.strategy;

import com.ansh.entity.Task;

import java.util.Comparator;

public class DeadlineSortStrategy implements TaskSortStrategy {

    @Override
    public Comparator<Task> getComparator() {

        return Comparator.comparing(
                Task::getDeadline,
                Comparator.nullsLast(
                        Comparator.naturalOrder()
                )
        );
    }
}