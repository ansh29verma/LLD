package com.ansh.locking;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InMemoryLockProvider implements LockProvider {
    private final ConcurrentHashMap<String, Expiry> locks = new ConcurrentHashMap<>();
    private final ScheduledExecutorService sweeper = Executors.newScheduledThreadPool(1);

    public InMemoryLockProvider() {
        sweeper.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            locks.entrySet().removeIf(entry -> entry.getValue().deadline < now);
        }, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public boolean tryLock(String key, long ttlMs, String userId) {
        long now = System.currentTimeMillis();
        Expiry newExpiry = new Expiry(now + ttlMs, userId);
        Expiry actual = locks.compute(key, (k, current) -> {
            if (current == null || current.deadline < now) {
                return newExpiry;
            }
            return current;
        });
        return actual == newExpiry;
    }

    @Override
    public void unlock(String key) {
        locks.remove(key);
    }

    @Override
    public boolean isLockExpired(String key) {
        Expiry expiry = locks.get(key);
        if (expiry == null) return true;
        return expiry.deadline < System.currentTimeMillis();
    }

    @Override
    public boolean isLockedBy(String key, String userId) {
        Expiry expiry = locks.get(key);
        if (expiry == null) return false;
        return expiry.owner.equals(userId);
    }

    public void shutdown() {
        sweeper.shutdown();
    }
}
