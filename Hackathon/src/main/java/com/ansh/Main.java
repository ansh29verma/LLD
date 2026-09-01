package com.ansh;





import com.ansh.dto.ProblemFilterCriteria;
import com.ansh.dto.ProblemResponseDto;
import com.ansh.entity.User;
import com.ansh.enums.Difficulty;
import com.ansh.enums.ProblemSortField;
import com.ansh.enums.SortOrder;
import com.ansh.repo.InMemoryProblemRepository;
import com.ansh.repo.InMemoryUserRepository;
import com.ansh.repo.ProblemRepository;
import com.ansh.repo.UserRepository;
import com.ansh.service.HackathonService;
import com.ansh.service.HackathonServiceImpl;
import com.ansh.strategy.BasePlusTimeScoringStrategy;
import com.ansh.strategy.RecommendationStrategy;
import com.ansh.strategy.ScoringStrategy;
import com.ansh.strategy.TagAndPopularityRecommendationStrategy;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Initialize Core Dependencies
        ProblemRepository problemRepo = new InMemoryProblemRepository();
        UserRepository userRepo = new InMemoryUserRepository();
        ScoringStrategy scoringStrategy = new BasePlusTimeScoringStrategy();
        RecommendationStrategy recommendationStrategy = new TagAndPopularityRecommendationStrategy();

        HackathonService service = new HackathonServiceImpl(problemRepo, userRepo, scoringStrategy, recommendationStrategy);

        // 1. Add Problems
        service.addProblem("P1", "Two Sum", "Find two numbers that add up to target", "Array", Difficulty.EASY, 100);
        service.addProblem("P2", "LRU Cache", "Design and implement LRU cache", "Data Structure", Difficulty.HARD, 300);
        service.addProblem("P3", "3Sum", "Find all unique triplets summing to zero", "Array", Difficulty.MEDIUM, 200);
        service.addProblem("P4", "Merge K Lists", "Merge k sorted linked lists", "Data Structure", Difficulty.HARD, 350);

        // 2. Add Users
        service.addUser("U1", "Alice", "Payments Core");
        service.addUser("U2", "Bob", "Mutual Funds");

        // 3. Fetch Problems by Filter & Sort
        System.out.println("\n--- Fetching Array Problems Sorted by Score DESC ---");
        List<ProblemResponseDto> filteredProblems = service.fetchProblems(
                ProblemFilterCriteria.builder()
                        .tag("Array")
                        .sortBy(ProblemSortField.SCORE)
                        .sortOrder(SortOrder.DESC)
                        .build()
        );
        filteredProblems.forEach(p -> System.out.println(p.getProblemId() + " -> " + p.getName() + " [Score: " + p.getBaseScore() + "]"));

        // 4. Solve Problem & Get Recommendations (Extension Requirement)
        System.out.println("\n--- Alice Solves P1 ---");
        List<ProblemResponseDto> recommendations = service.solve("U1", "P1", 15.0);
        System.out.println("Top Recommended Next Problems:");
        recommendations.forEach(r -> System.out.println(" - " + r.getProblemId() + ": " + r.getName() + " (" + r.getTag() + ")"));

        System.out.println("\n--- Bob Solves P2 and P4 ---");
        service.solve("U2", "P2", 45.0);
        service.solve("U2", "P4", 30.0);

        // 5. Get Solved Problems for User
        System.out.println("\n--- Fetching Solved Problems for Bob ---");
        List<ProblemResponseDto> bobsSolved = service.fetchSolvedProblems("U2");
        bobsSolved.forEach(p -> System.out.println(" - " + p.getProblemId() + " (" + p.getName() + ")"));

        // 6. Leaderboard Evaluation
        System.out.println("\n--- Current Leader ---");
        User leader = service.getLeader();
        System.out.println("Leader: " + leader.getName() + " | Dept: " + leader.getDepartment() + " | Score: " + leader.getTotalScore());

        // 7. Curation: Top N Liked Problems
        service.likeProblem("P1");
        service.likeProblem("P3");
        service.likeProblem("P3");

        System.out.println("\n--- Top Liked Problems in 'Array' Tag ---");
        List<ProblemResponseDto> topLiked = service.getTopNProblems("Array", 2);
        topLiked.forEach(p -> System.out.println(p.getName() + " -> Likes: " + p.getLikesCount()));
    }
}