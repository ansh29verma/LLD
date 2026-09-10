package com.ansh.entity;


import com.ansh.enums.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class Task {

    private String id;

    private final String userId;

    private String title;

    private String description;

    private LocalDateTime deadline;

    private LocalDateTime scheduledAt;

    @Builder.Default
    private Set<String> tags = new HashSet<>();

    @Builder.Default
    private TaskStatus status = TaskStatus.PENDING;

    private final LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime completedAt;



    public void setTags(Set<String> tags) {

        this.tags = tags == null
                ? new HashSet<>()
                : new HashSet<>(tags);
    }

}