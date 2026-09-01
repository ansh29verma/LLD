package com.ansh.repo;

import com.ansh.entity.FitnessClass;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryFitnessClassRepository implements FitnessClassRepository {
    private final Map<String, FitnessClass> storage = new ConcurrentHashMap<>();

    @Override
    public FitnessClass save(FitnessClass fitnessClass) {
        storage.put(fitnessClass.getClassId(), fitnessClass);
        return fitnessClass;
    }

    @Override
    public Optional<FitnessClass> findById(String classId) {
        return Optional.ofNullable(storage.get(classId));
    }

    @Override
    public List<FitnessClass> findAll() {
        return List.copyOf(storage.values());
    }
}
