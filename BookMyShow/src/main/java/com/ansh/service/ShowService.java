package com.ansh.service;

import com.ansh.entity.Movie;
import com.ansh.entity.Screen;
import com.ansh.entity.Show;
import com.ansh.entity.Theater;
import com.ansh.repo.ShowRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ShowService {
    private final ShowRepository showRepository;

    public ShowService(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    public Show createShow(String id, Movie movie, String startTimeStr, Theater theater, Screen screen) {
        Show show = new Show(id, movie, startTimeStr, theater, screen);
        showRepository.save(show);
        return show;
    }

    public Show getShow(String id) {
        return showRepository.get(id);
    }

    public List<Show> getShowsByMovieTitle(String title) {
        return showRepository.getAll().stream()
                .filter(show -> show.getMovie().getTitle().equalsIgnoreCase(title))
                .collect(Collectors.toList());
    }
}
