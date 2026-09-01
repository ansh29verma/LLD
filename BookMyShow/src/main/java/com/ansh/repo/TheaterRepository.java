package com.ansh.repo;

import com.ansh.entity.Theater;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TheaterRepository {
    private final Map<String, Theater> db = new HashMap<>();

    public void save(Theater theater) {
        db.put(theater.getId(), theater);
    }

    public Theater get(String id) {
        return db.get(id);
    }

    public Collection<Theater> getAll() {
        return db.values();
    }
}
