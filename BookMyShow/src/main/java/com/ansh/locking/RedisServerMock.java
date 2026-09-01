package com.ansh.locking;

import java.util.concurrent.ConcurrentHashMap;

class RedisServerMock {
    private static class RedisValue {
        final String value;
        final long expiresAt;

        RedisValue(String value, long expiresAt) {
            this.value = value;
            this.expiresAt = expiresAt;
        }
    }

    private final ConcurrentHashMap<String, RedisValue> storage = new ConcurrentHashMap<>();

    /**
     * Emulates: SET key value NX PX ttlMs
     * Atomic 'set-if-not-exists' with a millisecond Time-To-Live.
     */
    public synchronized boolean setNX(String key, String value, long ttlMs) {
        long now = System.currentTimeMillis();
        RedisValue current = storage.get(key);

        if (current == null || current.expiresAt < now) {
            storage.put(key, new RedisValue(value, now + ttlMs));
            return true;
        }
        return false;
    }

    /**
     * Emulates: GET key
     */
    public synchronized String get(String key) {
        long now = System.currentTimeMillis();
        RedisValue current = storage.get(key);
        if (current == null) return null;
        if (current.expiresAt < now) {
            storage.remove(key);
            return null;
        }
        return current.value;
    }

    /**
     * Emulates: DEL key
     */
    public synchronized void del(String key) {
        storage.remove(key);
    }

    /**
     * Emulates atomic Lua script execution used for secure release of locks.
     * script:
     *   if redis.call('get', KEYS[1]) == ARGV[1] then
     *       return redis.call('del', KEYS[1])
     *   else
     *       return 0
     *   end
     */
    public synchronized long executeLuaUnlock(String key, String expectedValue) {
        long now = System.currentTimeMillis();
        RedisValue current = storage.get(key);
        if (current != null && current.expiresAt >= now) {
            if (current.value.equals(expectedValue)) {
                storage.remove(key);
                return 1; // Success: Deleted/Unlocked
            }
        }
        return 0; // Failed: Lock either expired, doesn't exist, or is owned by another user
    }
}
