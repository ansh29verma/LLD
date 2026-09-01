package com.ansh.repo;


import com.ansh.entity.App;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAppRepository implements AppRepository {
    private final Map<String, App> storage = new ConcurrentHashMap<>();

    @Override
    public App save(App app) {
        storage.put(app.getAppId(), app);
        return app;
    }

    @Override
    public Optional<App> findById(String appId) {
        return Optional.ofNullable(storage.get(appId));
    }
}
