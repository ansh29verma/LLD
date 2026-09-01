package com.ansh.service;

import com.ansh.entity.Movie;
import com.ansh.repo.MovieRepository;

public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie createMovie(String id, String title, int durationInMinutes) {
        Movie movie = new Movie(id, title, durationInMinutes);
        movieRepository.save(movie);
        return movie;
    }

    public Movie getMovie(String id) {
        return movieRepository.get(id);
    }
}
