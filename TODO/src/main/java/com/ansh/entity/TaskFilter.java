package com.ansh.entity;

import com.ansh.enums.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class TaskFilter {

    private final String userId;

    private final Set<String> tags;

    private final TaskStatus status;

    private final LocalDateTime deadlineBefore;

    private final LocalDateTime deadlineAfter;

    private final LocalDateTime scheduledBefore;

    private final LocalDateTime scheduledAfter;

    @Builder.Default
    private final boolean includeFutureTasks = false;
}
