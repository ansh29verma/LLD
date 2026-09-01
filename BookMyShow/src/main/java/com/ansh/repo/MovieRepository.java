package com.ansh.repo;

import com.ansh.entity.Movie;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MovieRepository {
    private final Map<String, Movie> db = new HashMap<>();

    public void save(Movie movie) {
        db.put(movie.getId(), movie);
    }

    public Movie get(String id) {
        return db.get(id);
    }

    public Collection<Movie> getAll() {
        return db.values();
    }
}
