package com.ansh.EvictionAlgorithms;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class TTLEvictionAlgorithm<K> implements EvictionAlgorithm<K> {

    // Configurable default survival duration for keys in milliseconds
    private final long ttlDurationMs;

    // Map to find the current expiration timestamp of any given key instantly
    private final Map<K, Long> keyToExpiryMap;

    // Sorted map tracking timestamp -> key to find the earliest expiring item in O(log N)
    private final TreeMap<Long, K> expiryToKeyMap;

    public TTLEvictionAlgorithm(long ttlDurationMs) {
        this.ttlDurationMs = ttlDurationMs;
        this.keyToExpiryMap = new HashMap<>();
        this.expiryToKeyMap = new TreeMap<>();
    }

    @Override
    public synchronized void keyAccessed(K key) throws Exception {
        // If the key already exists, clean up its old expiration entry first
        if (keyToExpiryMap.containsKey(key)) {
            long oldExpiry = keyToExpiryMap.remove(key);
            expiryToKeyMap.remove(oldExpiry);
        }

        // Compute new expiration target timestamp
        long newExpiryTime = System.currentTimeMillis() + ttlDurationMs;

        // Track the structural metadata
        keyToExpiryMap.put(key, newExpiryTime);
        expiryToKeyMap.put(newExpiryTime, key);
    }

    @Override
    public synchronized K evictKey() throws Exception {
        if (expiryToKeyMap.isEmpty()) {
            return null;
        }

        // The first entry in a TreeMap always contains the lowest value (oldest/earliest expiration)
        Map.Entry<Long, K> earliestExpiryEntry = expiryToKeyMap.firstEntry();
        K targetKey = earliestExpiryEntry.getValue();

        // Completely scrub references from the algorithm mappings
        expiryToKeyMap.remove(earliestExpiryEntry.getKey());
        keyToExpiryMap.remove(targetKey);

        return targetKey;
    }
}

