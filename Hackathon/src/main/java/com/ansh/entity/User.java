package com.ansh.entity;



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
public class User {
    private String userId;
    private String name;
    private String department;

    @Builder.Default
    private double totalScore = 0.0;

    @Builder.Default
    private Set<String> solvedProblemIds = new HashSet<>();
}
