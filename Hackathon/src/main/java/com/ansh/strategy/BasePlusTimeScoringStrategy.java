package com.ansh.strategy;


import com.ansh.entity.Problem;

public class BasePlusTimeScoringStrategy implements ScoringStrategy {

    @Override
    public double calculateScore(Problem problem, double timeTakenMinutes) {
        // Base score bonus minus time penalty (faster solutions get slightly higher points)
        double timePenalty = Math.min(problem.getBaseScore() * 0.5, timeTakenMinutes * 0.1);
        return Math.max(1.0, problem.getBaseScore() - timePenalty);
    }
}