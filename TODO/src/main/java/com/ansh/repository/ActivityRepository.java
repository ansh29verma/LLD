package com.ansh.repository;

import com.ansh.entity.Activity;

import java.util.List;

public interface ActivityRepository {
    void save(Activity activity);
    List<Activity> findAll();
}
