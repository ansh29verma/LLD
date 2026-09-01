package com.ansh.entity;




import com.ansh.enums.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Problem {
    private String problemId;
    private String name;
    private String description;
    private String tag;
    private Difficulty difficulty;
    private double baseScore;

    @Builder.Default
    private int likesCount = 0;

    @Builder.Default
    private Set<String> solvedUserIds = new HashSet<>();

    @Builder.Default
    private double totalTimeSpentInMinutes = 0.0;

    public int getSolvedCount() {
        return solvedUserIds.size();
    }

    public double getAverageTimeTaken() {
        return solvedUserIds.isEmpty() ? 0.0 : totalTimeSpentInMinutes / solvedUserIds.size();
    }
}