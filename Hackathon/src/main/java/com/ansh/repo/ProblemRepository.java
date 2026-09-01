package com.ansh.repo;


import com.ansh.entity.Problem;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository {
    Problem save(Problem problem);
    Optional<Problem> findById(String problemId);
    List<Problem> findAll();
}