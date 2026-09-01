package com.ansh.repo;



import com.ansh.entity.Problem;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryProblemRepository implements ProblemRepository {
    private final Map<String, Problem> storage = new ConcurrentHashMap<>();

    @Override
    public Problem save(Problem problem) {
        storage.put(problem.getProblemId(), problem);
        return problem;
    }

    @Override
    public Optional<Problem> findById(String problemId) {
        return Optional.ofNullable(storage.get(problemId));
    }

    @Override
    public List<Problem> findAll() {
        return List.copyOf(storage.values());
    }
}
