package com.ansh.repo;

// File: com/uber/repository/FareRepository.java

import com.ansh.entity.Fare;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.*;

public class FareRepository {
    @Getter
    @AllArgsConstructor
    private static class FareEntity {
        private final String riderId;
        private final Fare fare;
        private final long expiresAt;
    }

    private final Map<String, FareEntity> cache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleanupExecutor = Executors.newSingleThreadScheduledExecutor();
    private final long ttlMillis;

    public FareRepository(long ttlMillis) {
        this.ttlMillis = ttlMillis;
        this.cleanupExecutor.scheduleAtFixedRate(this::evictExpiredFares, 1, 1, TimeUnit.MINUTES);
    }

    public void save(Fare fare, String riderId) {
        long expiry = System.currentTimeMillis() + ttlMillis;
        cache.put(fare.getId(), new FareEntity(riderId, fare, expiry));
    }

    public Fare getValidFare(String fareId, String riderId) {
        FareEntity entity = cache.get(fareId);
        if (entity == null) return null;

        if (!entity.getRiderId().equals(riderId) || entity.getExpiresAt() < System.currentTimeMillis()) {
            cache.remove(fareId);
            return null;
        }
        return entity.getFare();
    }

    private void evictExpiredFares() {
        long now = System.currentTimeMillis();
        cache.entrySet().removeIf(entry -> entry.getValue().getExpiresAt() < now);
    }

    public void shutdown() {
        cleanupExecutor.shutdown();
    }
}