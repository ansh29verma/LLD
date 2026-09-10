package com.ansh.repo;

// File: com/uber/repository/DriverRepository.java


import com.ansh.entity.Driver;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DriverRepository {
    private final Map<String, Driver> store = new ConcurrentHashMap<>();

    public void save(Driver driver) { store.put(driver.getId(), driver); }
    public Driver findById(String id) { return store.get(id); }
    public Collection<Driver> findAll() { return store.values(); }
}