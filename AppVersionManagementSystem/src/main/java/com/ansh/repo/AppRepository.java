package com.ansh.repo;


import com.ansh.entity.App;

import java.util.Optional;

public interface AppRepository {
    App save(App app);
    Optional<App> findById(String appId);
}
