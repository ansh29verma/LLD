package com.ansh.locking;

public class RedisLockProvider implements LockProvider {
    private final RedisServerMock redisClient;

    public RedisLockProvider(RedisServerMock redisClient) {
        this.redisClient = redisClient;
    }

    @Override
    public boolean tryLock(String key, long ttlMs, String userId) {
        // SET key userId NX PX ttlMs
        boolean locked = redisClient.setNX(key, userId, ttlMs);
        if (locked) {
            System.out.println("[Redis Lock] ACQUIRED Lock for Key: " + key + " | Owner: " + userId + " | TTL: " + ttlMs + "ms");
        } else {
            System.out.println("[Redis Lock] FAILED to acquire Lock for Key: " + key + " | Already locked by someone else.");
        }
        return locked;
    }

    @Override
    public void unlock(String key) {
        String userId = redisClient.get(key);
        // Execute the atomic Lua unlock script to ensure a worker ONLY releases its own locks
        long result = redisClient.executeLuaUnlock(key, userId);
        if (result == 1) {
            System.out.println("[Redis Lock] RELEASED Lock for Key: " + key + " | Owner: " + userId);
        } else {
            System.out.println("[Redis Lock] RELEASE FAILED (Forbidden/Expired) for Key: " + key + " | Tried by: " + userId);
        }
    }

    @Override
    public boolean isLockExpired(String key) {
        return redisClient.get(key) == null;
    }

    @Override
    public boolean isLockedBy(String key, String userId) {
        String currentOwner = redisClient.get(key);
        return userId.equals(currentOwner);
    }
}