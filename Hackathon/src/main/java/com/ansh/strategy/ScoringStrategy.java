package com.ansh.strategy;

import com.ansh.entity.Problem;

public interface ScoringStrategy {
    double calculateScore(Problem problem, double timeTakenMinutes);
}
