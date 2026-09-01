package com.ansh.strategy;



import com.ansh.entity.Problem;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TagAndPopularityRecommendationStrategy implements RecommendationStrategy {

    @Override
    public List<Problem> recommend(Problem solvedProblem, List<Problem> allProblems, int topN) {
        return allProblems.stream()
                .filter(p -> !p.getProblemId().equalsIgnoreCase(solvedProblem.getProblemId()))
                .sorted(Comparator
                        .comparing((Problem p) -> p.getTag().equalsIgnoreCase(solvedProblem.getTag()) ? 1 : 0)
                        .thenComparingInt(Problem::getSolvedCount)
                        .thenComparingDouble(Problem::getBaseScore)
                        .reversed())
                .limit(topN)
                .collect(Collectors.toList());
    }
}
