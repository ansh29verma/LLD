package com.ansh.dto;

import com.ansh.entity.Problem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProblemResponseDto {
    private String problemId;
    private String name;
    private String description;
    private String tag;
    private String difficulty;
    private double baseScore;
    private int likesCount;
    private int solvedCount;
    private double averageTimeTakenMinutes;

    public static ProblemResponseDto fromEntity(Problem p) {
        return ProblemResponseDto.builder()
                .problemId(p.getProblemId())
                .name(p.getName())
                .description(p.getDescription())
                .tag(p.getTag())
                .difficulty(p.getDifficulty().name())
                .baseScore(p.getBaseScore())
                .likesCount(p.getLikesCount())
                .solvedCount(p.getSolvedCount())
                .averageTimeTakenMinutes(p.getAverageTimeTaken())
                .build();
    }
}
