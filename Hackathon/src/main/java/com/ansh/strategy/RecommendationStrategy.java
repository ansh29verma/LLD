package com.ansh.strategy;

import com.ansh.entity.Problem;

import java.util.List;

public interface RecommendationStrategy {
    List<Problem> recommend(Problem solvedProblem, List<Problem> allProblems, int topN);
}
