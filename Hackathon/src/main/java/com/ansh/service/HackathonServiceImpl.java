package com.ansh.service;





import com.ansh.dto.ProblemFilterCriteria;
import com.ansh.dto.ProblemResponseDto;
import com.ansh.entity.Problem;
import com.ansh.entity.User;
import com.ansh.enums.Difficulty;
import com.ansh.enums.ErrorCode;
import com.ansh.enums.SortOrder;
import com.ansh.exception.HackathonException;
import com.ansh.repo.ProblemRepository;
import com.ansh.repo.UserRepository;
import com.ansh.strategy.RecommendationStrategy;
import com.ansh.strategy.ScoringStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HackathonServiceImpl implements HackathonService {

    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final ScoringStrategy scoringStrategy;
    private final RecommendationStrategy recommendationStrategy;

    public HackathonServiceImpl(ProblemRepository problemRepository,
                                UserRepository userRepository,
                                ScoringStrategy scoringStrategy,
                                RecommendationStrategy recommendationStrategy) {
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
        this.scoringStrategy = scoringStrategy;
        this.recommendationStrategy = recommendationStrategy;
    }

    @Override
    public void addProblem(String problemId, String name, String description, String tag, Difficulty difficulty, double score) {
        if (problemRepository.findById(problemId).isPresent()) {
            throw new HackathonException(ErrorCode.PROBLEM_ALREADY_EXISTS);
        }

        Problem problem = Problem.builder()
                .problemId(problemId)
                .name(name)
                .description(description)
                .tag(tag)
                .difficulty(difficulty)
                .baseScore(score)
                .build();

        problemRepository.save(problem);
        System.out.println(">>> Problem " + problemId + " added to Question Library.");
    }

    @Override
    public void addUser(String userId, String name, String department) {
        if (userRepository.findById(userId).isPresent()) {
            throw new HackathonException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = User.builder()
                .userId(userId)
                .name(name)
                .department(department)
                .build();

        userRepository.save(user);
        System.out.println(">>> User " + name + " (" + department + ") registered successfully.");
    }

    @Override
    public List<ProblemResponseDto> fetchProblems(ProblemFilterCriteria criteria) {
        List<Problem> problems = problemRepository.findAll();

        return problems.stream()
                .filter(p -> criteria.getDifficulty() == null || p.getDifficulty() == criteria.getDifficulty())
                .filter(p -> criteria.getTag() == null || p.getTag().equalsIgnoreCase(criteria.getTag()))
                .sorted((p1, p2) -> {
                    int result = 0;
                    if (criteria.getSortBy() != null) {
                        switch (criteria.getSortBy()) {
                            case SCORE -> result = Double.compare(p1.getBaseScore(), p2.getBaseScore());
                            case DIFFICULTY -> result = p1.getDifficulty().compareTo(p2.getDifficulty());
                            case SOLVED_COUNT -> result = Integer.compare(p1.getSolvedCount(), p2.getSolvedCount());
                        }
                    } else {
                        result = Double.compare(p1.getBaseScore(), p2.getBaseScore());
                    }
                    return criteria.getSortOrder() == SortOrder.DESC ? -result : result;
                })
                .map(ProblemResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProblemResponseDto> solve(String userId, String problemId, double timeTakenMinutes) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new HackathonException(ErrorCode.USER_NOT_FOUND));

        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new HackathonException(ErrorCode.PROBLEM_NOT_FOUND));

        if (user.getSolvedProblemIds().contains(problemId)) {
            throw new HackathonException(ErrorCode.PROBLEM_ALREADY_SOLVED);
        }

        double awardedScore = scoringStrategy.calculateScore(problem, timeTakenMinutes);

        // Update User
        user.getSolvedProblemIds().add(problemId);
        user.setTotalScore(user.getTotalScore() + awardedScore);
        userRepository.save(user);

        // Update Problem statistics
        problem.getSolvedUserIds().add(userId);
        problem.setTotalTimeSpentInMinutes(problem.getTotalTimeSpentInMinutes() + timeTakenMinutes);
        problemRepository.save(problem);

        System.out.println(String.format(">>> User %s solved Problem %s! Awarded Score: %.2f", user.getName(), problemId, awardedScore));

        // Extension: Fetch top 5 recommendations
        List<Problem> recommendations = recommendationStrategy.recommend(problem, problemRepository.findAll(), 5);
        return recommendations.stream().map(ProblemResponseDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<ProblemResponseDto> fetchSolvedProblems(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new HackathonException(ErrorCode.USER_NOT_FOUND));

        return user.getSolvedProblemIds().stream()
                .map(id -> problemRepository.findById(id).orElse(null))
                .filter(p -> p != null)
                .map(ProblemResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public User getLeader() {
        return userRepository.findAll().stream()
                .max(Comparator.comparingDouble(User::getTotalScore))
                .orElseThrow(() -> new HackathonException(ErrorCode.USER_NOT_FOUND, "No users present in system"));
    }

    @Override
    public List<ProblemResponseDto> getTopNProblems(String tag, int n) {
        return problemRepository.findAll().stream()
                .filter(p -> tag == null || p.getTag().equalsIgnoreCase(tag))
                .sorted(Comparator.comparingInt(Problem::getLikesCount).reversed())
                .limit(n)
                .map(ProblemResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void likeProblem(String problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new HackathonException(ErrorCode.PROBLEM_NOT_FOUND));
        problem.setLikesCount(problem.getLikesCount() + 1);
        problemRepository.save(problem);
    }
}
