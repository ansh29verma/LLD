package com.ansh.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Statistics {

    private final long tasksAdded;

    private final long tasksCompleted;

    private final long tasksSpilledOverDeadline;
}
