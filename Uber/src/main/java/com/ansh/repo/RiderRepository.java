package com.ansh.repo;

// File: com/uber/repository/RideRepository.java
// File: com/uber/repository/RiderRepository.java



import com.ansh.entity.Rider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RiderRepository {
    private final Map<String, Rider> store = new ConcurrentHashMap<>();

    public void save(Rider rider) { store.put(rider.getId(), rider); }
    public Rider findById(String id) { return store.get(id); }
}
