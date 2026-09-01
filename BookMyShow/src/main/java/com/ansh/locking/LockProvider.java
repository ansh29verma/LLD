package com.ansh.locking;

public interface LockProvider {
    boolean tryLock(String key, long ttlMs, String userId);
    void unlock(String key);
    boolean isLockExpired(String key);
    boolean isLockedBy(String key, String userId);
}
