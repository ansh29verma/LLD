package com.ansh.entity;

import com.ansh.enums.ActivityType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Activity {

    private final String taskId;

    private final String userId;

    private final ActivityType type;

    private final LocalDateTime timestamp;
}
