package com.ansh.repo;

// File: com/uber/repository/RideRepository.java

import com.ansh.entity.Ride;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RideRepository {
    private final Map<String, Ride> store = new ConcurrentHashMap<>();

    public void save(Ride ride) { store.put(ride.getId(), ride); }
    public Ride findById(String id) { return store.get(id); }
}