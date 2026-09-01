package com.ansh.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class Theater {
    private final String id;
    private final String name;
    private final Map<String, Screen> screens = new HashMap<>();

    public void addScreen(Screen screen) {
        screens.put(screen.getId(), screen);
    }

    public Screen getScreen(String id) {
        return screens.get(id);
    }
}
