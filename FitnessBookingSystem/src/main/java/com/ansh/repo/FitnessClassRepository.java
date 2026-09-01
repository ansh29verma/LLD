package com.ansh.repo;

import com.ansh.entity.FitnessClass;

import java.util.List;
import java.util.Optional;

public interface FitnessClassRepository {
    FitnessClass save(FitnessClass fitnessClass);
    Optional<FitnessClass> findById(String classId);
    List<FitnessClass> findAll();
}
