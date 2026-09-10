package com.ansh.repository;


import com.ansh.entity.Activity;

import java.util.ArrayList;
import java.util.List;

public class InMemoryActivityRepository implements ActivityRepository {

    private final List<Activity> activities = new ArrayList<>();

    @Override
    public void save(Activity activity) {
        activities.add(activity);
    }

    @Override
    public List<Activity> findAll() {
        return new ArrayList<>(activities);
    }
}