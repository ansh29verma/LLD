package com.ansh.strategy;

import com.ansh.entity.Task;

import java.util.Comparator;

public class CreatedAtSortStrategy implements TaskSortStrategy {

    @Override
    public Comparator<Task> getComparator() {

        return Comparator.comparing(
                Task::getCreatedAt,
                Comparator.nullsLast(
                        Comparator.naturalOrder()
                )
        );
    }
}