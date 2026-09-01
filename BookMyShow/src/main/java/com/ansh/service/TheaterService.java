package com.ansh.service;

import com.ansh.entity.Screen;
import com.ansh.entity.Seat;
import com.ansh.entity.Theater;
import com.ansh.repo.TheaterRepository;

public class TheaterService {
    private final TheaterRepository theaterRepository;

    public TheaterService(TheaterRepository theaterRepository) {
        this.theaterRepository = theaterRepository;
    }

    public Theater createTheater(String id, String name) {
        Theater theater = new Theater(id, name);
        theaterRepository.save(theater);
        return theater;
    }

    public void addScreenToTheater(String theaterId, Screen screen) {
        Theater theater = theaterRepository.get(theaterId);
        if (theater != null) {
            theater.addScreen(screen);
        }
    }

    public void addSeatToScreen(String theaterId, String screenId, Seat seat) {
        Theater theater = theaterRepository.get(theaterId);
        if (theater != null) {
            Screen screen = theater.getScreen(screenId);
            if (screen != null) {
                screen.addSeat(seat);
            }
        }
    }
}
