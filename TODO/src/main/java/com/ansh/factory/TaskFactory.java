package com.ansh.factory;

import com.ansh.entity.Task;
import com.ansh.enums.TaskStatus;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class TaskFactory {

    public Task create(
            String userId,
            String title,
            String description,
            LocalDateTime deadline,
            LocalDateTime scheduledAt,
            Set<String> tags
    ) {

        LocalDateTime now =
                LocalDateTime.now();

        return Task.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .title(title)
                .description(description)
                .deadline(deadline)
                .scheduledAt(
                        scheduledAt == null
                                ? now
                                : scheduledAt
                )
                .tags(
                        tags == null
                                ? new HashSet<>()
                                : new HashSet<>(tags)
                )
                .status(TaskStatus.PENDING)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
}
