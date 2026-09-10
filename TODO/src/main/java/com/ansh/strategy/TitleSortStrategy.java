package com.ansh.strategy;

import com.ansh.entity.Task;

import java.util.Comparator;

public class TitleSortStrategy
        implements TaskSortStrategy {

    @Override
    public Comparator<Task> getComparator() {

        return Comparator.comparing(
                Task::getTitle,
                Comparator.nullsLast(
                        String.CASE_INSENSITIVE_ORDER
                )
        );
    }
}