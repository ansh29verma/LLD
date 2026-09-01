package com.ansh.repo;


import com.ansh.entity.Patch;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPatchRepository implements PatchRepository {
    private final Map<String, Patch> storage = new ConcurrentHashMap<>();

    @Override
    public Patch save(Patch patch) {
        String key = buildKey(patch.getAppId(), patch.getFromVersion(), patch.getToVersion());
        storage.put(key, patch);
        return patch;
    }

    @Override
    public Optional<Patch> findPatch(String appId, String fromVersion, String toVersion) {
        return Optional.ofNullable(storage.get(buildKey(appId, fromVersion, toVersion)));
    }

    private String buildKey(String appId, String fromVersion, String toVersion) {
        return appId + ":" + fromVersion + "->" + toVersion;
    }
}
