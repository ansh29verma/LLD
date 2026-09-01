package com.ansh.service;

import com.ansh.dto.ProblemFilterCriteria;
import com.ansh.dto.ProblemResponseDto;
import com.ansh.entity.User;
import com.ansh.enums.Difficulty;

import java.util.List;

public interface HackathonService {
    void addProblem(String problemId, String name, String description, String tag, Difficulty difficulty, double score);
    void addUser(String userId, String name, String department);
    List<ProblemResponseDto> fetchProblems(ProblemFilterCriteria criteria);
    List<ProblemResponseDto> solve(String userId, String problemId, double timeTakenMinutes);
    List<ProblemResponseDto> fetchSolvedProblems(String userId);
    User getLeader();
    List<ProblemResponseDto> getTopNProblems(String tag, int n);
    void likeProblem(String problemId);

}
