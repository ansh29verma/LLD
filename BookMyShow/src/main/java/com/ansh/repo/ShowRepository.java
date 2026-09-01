package com.ansh.repo;

import com.ansh.entity.Show;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ShowRepository {
    private final Map<String, Show> db = new HashMap<>();

    public void save(Show show) {
        db.put(show.getId(), show);
    }

    public Show get(String id) {
        return db.get(id);
    }

    public Collection<Show> getAll() {
        return db.values();
    }
}
