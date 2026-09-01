package com.ansh.repo;

import com.ansh.entity.Patch;

import java.util.Optional;

public interface PatchRepository {
    Patch save(Patch patch);
    Optional<Patch> findPatch(String appId, String fromVersion, String toVersion);
}

