package com.ansh.entity;



import com.ansh.enums.ClassType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FitnessClass {
    private String classId;
    private String title;
    private ClassType classType; // e.g., "YOGA", "GYM", "DANCE"
    private int capacity;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Builder.Default
    private boolean cancelled = false;

    @Builder.Default
    private List<String> confirmedUserIds = new LinkedList<>();

    @Builder.Default
    private Queue<String> waitlistedUserIds = new LinkedList<>();
}
