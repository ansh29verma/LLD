package com.ansh.service;

import com.ansh.client.DeviceInstallerClient;
import com.ansh.client.StorageClient;
import com.ansh.entity.App;
import com.ansh.entity.AppVersion;
import com.ansh.entity.Device;
import com.ansh.entity.Patch;
import com.ansh.enums.ErrorCode;
import com.ansh.enums.OsType;
import com.ansh.enums.TaskType;
import com.ansh.exception.VersionManagementException;
import com.ansh.repo.AppRepository;
import com.ansh.repo.PatchRepository;
import com.ansh.strategy.RolloutStrategy;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class AppVersionServiceImpl implements AppVersionService {

    private final AppRepository appRepository;
    private final PatchRepository patchRepository;
    private final StorageClient storageClient;
    private final DeviceInstallerClient deviceInstallerClient;
    private final Map<String, RolloutStrategy> activeRollouts = new ConcurrentHashMap<>();

    public AppVersionServiceImpl(AppRepository appRepository,
                                 PatchRepository patchRepository,
                                 StorageClient storageClient,
                                 DeviceInstallerClient deviceInstallerClient) {
        this.appRepository = appRepository;
        this.patchRepository = patchRepository;
        this.storageClient = storageClient;
        this.deviceInstallerClient = deviceInstallerClient;
    }

    @Override
    public void uploadNewVersion(String appId, String versionId, OsType osType, int minOsVersion, byte[] fileContent) {
        App app = appRepository.findById(appId).orElseGet(() -> {
            App newApp = App.builder().appId(appId).name(appId).build();
            return appRepository.save(newApp);
        });

        String fileUrl = storageClient.uploadFile(fileContent);

        AppVersion newVersion = AppVersion.builder()
                .versionId(versionId)
                .appId(appId)
                .osType(osType)
                .minOsVersion(minOsVersion)
                .fileUrl(fileUrl)
                .releaseDate(LocalDateTime.now())
                .build();

        app.getVersions().put(versionId, newVersion);
        appRepository.save(app);
        System.out.println(String.format("Uploaded App '%s' Version '%s' for OS '%s' (Min OS: %d)",
                appId, versionId, osType, minOsVersion));
    }

    @Override
    public String createUpdatePatch(String appId, String fromVersion, String toVersion) {
        App app = getAppOrThrow(appId);
        AppVersion source = getVersionOrThrow(app, fromVersion);
        AppVersion target = getVersionOrThrow(app, toVersion);

        byte[] sourceBytes = storageClient.getFile(source.getFileUrl());
        byte[] targetBytes = storageClient.getFile(target.getFileUrl());

        byte[] diffBytes = storageClient.createDiffPack(sourceBytes, targetBytes);
        String patchUrl = storageClient.uploadFile(diffBytes);

        Patch patch = Patch.builder()
                .patchId("PATCH-" + System.nanoTime())
                .appId(appId)
                .fromVersion(fromVersion)
                .toVersion(toVersion)
                .patchUrl(patchUrl)
                .build();

        patchRepository.save(patch);
        System.out.println(String.format("Created Patch for '%s': %s -> %s", appId, fromVersion, toVersion));
        return patchUrl;
    }

    @Override
    public void releaseVersion(String appId, String versionId, RolloutStrategy strategy) {
        App app = getAppOrThrow(appId);
        getVersionOrThrow(app, versionId);

        activeRollouts.put(appId + ":" + versionId, strategy);
        System.out.println(String.format("Released Version '%s' for App '%s' with Strategy '%s'",
                versionId, appId, strategy.getClass().getSimpleName()));
    }

    @Override
    public boolean isAppVersionSupported(String appId, String versionId, Device device, RolloutStrategy strategy) {
        App app = getAppOrThrow(appId);
        AppVersion targetVersion = getVersionOrThrow(app, versionId);

        if (device.getOsType() != targetVersion.getOsType()) {
            return false;
        }

        if (device.getOsVersion() < targetVersion.getMinOsVersion()) {
            return false;
        }

        if (strategy != null && !strategy.isEligible(device)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean checkForInstall(String appId, Device device) {
        App app = getAppOrThrow(appId);
        return app.getVersions().values().stream()
                .filter(v -> v.getOsType() == device.getOsType())
                .anyMatch(v -> device.getOsVersion() >= v.getMinOsVersion());
    }

    @Override
    public Optional<AppVersion> checkForUpdates(String appId, String installedVersionId, Device device) {
        App app = getAppOrThrow(appId);
        AppVersion installedVersion = getVersionOrThrow(app, installedVersionId);

        return app.getVersions().values().stream()
                .filter(v -> v.getOsType() == device.getOsType())
                .filter(v -> device.getOsVersion() >= v.getMinOsVersion())
                .filter(v -> v.compareTo(installedVersion) > 0)
                .filter(v -> {
                    RolloutStrategy strategy = activeRollouts.get(appId + ":" + v.getVersionId());
                    return strategy == null || strategy.isEligible(device);
                })
                .max(Comparator.naturalOrder());
    }

    @Override
    public void executeTask(TaskType taskType, String appId, Device device, String currentVersion, String targetVersion) {
        App app = getAppOrThrow(appId);
        AppVersion targetAppVersion = getVersionOrThrow(app, targetVersion);

        RolloutStrategy strategy = activeRollouts.get(appId + ":" + targetVersion);
        if (!isAppVersionSupported(appId, targetVersion, device, strategy)) {
            throw new VersionManagementException(ErrorCode.UNSUPPORTED_DEVICE,
                    "Target version is not supported on this device.");
        }

        if (taskType == TaskType.INSTALL) {
            byte[] binary = storageClient.getFile(targetAppVersion.getFileUrl());
            deviceInstallerClient.installApp(device, binary);
        } else if (taskType == TaskType.UPDATE) {
            Optional<Patch> patchOpt = patchRepository.findPatch(appId, currentVersion, targetVersion);
            byte[] patchBytes;

            if (patchOpt.isPresent()) {
                patchBytes = storageClient.getFile(patchOpt.get().getPatchUrl());
            } else {
                // On-demand patch creation
                createUpdatePatch(appId, currentVersion, targetVersion);
                Patch generatedPatch = patchRepository.findPatch(appId, currentVersion, targetVersion)
                        .orElseThrow(() -> new VersionManagementException(ErrorCode.PATCH_CREATION_FAILED));
                patchBytes = storageClient.getFile(generatedPatch.getPatchUrl());
            }

            deviceInstallerClient.updateApp(device, patchBytes);
        }
    }

    private App getAppOrThrow(String appId) {
        return appRepository.findById(appId)
                .orElseThrow(() -> new VersionManagementException(ErrorCode.APP_NOT_FOUND));
    }

    private AppVersion getVersionOrThrow(App app, String versionId) {
        AppVersion version = app.getVersions().get(versionId);
        if (version == null) {
            throw new VersionManagementException(ErrorCode.VERSION_NOT_FOUND, "Version " + versionId + " not found.");
        }
        return version;
    }
}