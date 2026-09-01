package com.ansh.service;

import com.ansh.entity.AppVersion;
import com.ansh.entity.Device;
import com.ansh.enums.OsType;
import com.ansh.enums.TaskType;
import com.ansh.strategy.RolloutStrategy;

import java.util.Optional;

public interface AppVersionService {
    void uploadNewVersion(String appId, String versionId, OsType osType, int minOsVersion, byte[] fileContent);
    String createUpdatePatch(String appId, String fromVersion, String toVersion);
    void releaseVersion(String appId, String versionId, RolloutStrategy strategy);
    boolean isAppVersionSupported(String appId, String versionId, Device device, RolloutStrategy strategy);
    boolean checkForInstall(String appId, Device device);
    Optional<AppVersion> checkForUpdates(String appId, String installedVersion, Device device);
    void executeTask(TaskType taskType, String appId, Device device, String currentVersion, String targetVersion);
}

