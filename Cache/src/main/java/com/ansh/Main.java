package com.ansh;

import com.ansh.EvictionAlgorithms.EvictionAlgorithm;
import com.ansh.EvictionAlgorithms.LRUEvictionAlgorithm;
import com.ansh.EvictionAlgorithms.TTLEvictionAlgorithm;
import com.ansh.Storage.CacheStorage;
import com.ansh.Storage.CacheStorageImpl;
import com.ansh.Storage.DBStorage;
import com.ansh.Storage.DBStorageImpl;
import com.ansh.writepolicy.WritePolicy;
import com.ansh.writepolicy.WriteThroughPolicy;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        try {
            // Set a small capacity for the in-memory cache (e.g., 5 items)
            CacheStorage<String, String> cacheStorage = new CacheStorageImpl<>(5);
            // The underlying persistent store (DB storage) can be assumed to have large or unlimited capacity.
            DBStorage<String, String> dbStorage = new DBStorageImpl<>();
            // Create the write-through policy (writes concurrently to both storages).
            WritePolicy<String, String> writePolicy = new WriteThroughPolicy<>();
            // Create the LRU eviction algorithm.
            EvictionAlgorithm<String> evictionAlg = new LRUEvictionAlgorithm<>();

            EvictionAlgorithm<String> evictionAlg2 = new TTLEvictionAlgorithm<>(1);
            // Create the cache with 4 executor threads to guarantee per-key ordering.
            Cache<String, String> cache = new Cache<>(cacheStorage, dbStorage, writePolicy, evictionAlg2, 4);

            // Demonstrate write operations.
            cache.updateData("A", "Apple").join();
            cache.updateData("B", "Banana").join();
            cache.updateData("C", "Cherry").join();
            cache.updateData("D", "Durian").join();
            cache.updateData("E", "Elderberry").join();

            // At this point, the in-memory cache is at capacity.
            // The next write will trigger eviction (of the least recently used key) from the cache.
            cache.updateData("F", "Fig").join();

            // Demonstrate read operations.
            try {
                String valueA = cache.accessData("A").join();
                System.out.println("A: " + valueA);
            } catch(Exception e) {
                System.out.println("A is evicted or not found in cache.");
            }

            String valueF = cache.accessData("F").join();
            System.out.println("F: " + valueF);

            // Update an existing key and then read it to demonstrate read-your-own-writes.
            cache.updateData("B", "Blueberry").join();
            String valueB = cache.accessData("B").join();
            System.out.println("B: " + valueB);

//            System.out.println(cache.);

            // Shut down executors when finished.
            cache.shutdown();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}