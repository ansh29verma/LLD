package com.ansh;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.



import com.ansh.client.DeviceInstallerClient;
import com.ansh.client.MockDeviceInstallerClient;
import com.ansh.client.MockStorageClient;
import com.ansh.client.StorageClient;
import com.ansh.entity.AppVersion;
import com.ansh.entity.Device;
import com.ansh.enums.OsType;
import com.ansh.enums.TaskType;
import com.ansh.repo.*;
import com.ansh.service.AppVersionService;
import com.ansh.service.AppVersionServiceImpl;
import com.ansh.strategy.BetaRolloutStrategy;

import java.util.Optional;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        // Instantiate repositories and infrastructure clients
        AppRepository appRepo = new InMemoryAppRepository();
        PatchRepository patchRepo = new InMemoryPatchRepository();
        DeviceRepository deviceRepo = new InMemoryDeviceRepository();
        StorageClient storageClient = new MockStorageClient();
        DeviceInstallerClient installerClient = new MockDeviceInstallerClient();

        // Instantiate domain service
        AppVersionService service = new AppVersionServiceImpl(appRepo, patchRepo, storageClient, installerClient);

        // 1. Upload Versions for PhonePe
        byte[] v1Binary = "PhonePe-V1.0.0-BinaryPayload".getBytes();
        byte[] v2Binary = "PhonePe-V2.0.0-BinaryPayloadWithDarkMode".getBytes();

        service.uploadNewVersion("PhonePe", "1.0.0", OsType.ANDROID, 10, v1Binary);
        service.uploadNewVersion("PhonePe", "2.0.0", OsType.ANDROID, 12, v2Binary);

        // 2. Pre-create an Update Patch
        service.createUpdatePatch("PhonePe", "1.0.0", "2.0.0");

        // 3. Register Devices
        Device dev1 = Device.builder().deviceId("DEV_ALPHA").osType(OsType.ANDROID).osVersion(13).build();
        Device dev2 = Device.builder().deviceId("DEV_BETA").osType(OsType.ANDROID).osVersion(11).build();
        deviceRepo.save(dev1);
        deviceRepo.save(dev2);

        // 4. Fresh Install Scenario
        System.out.println("\n--- FRESH INSTALL SCENARIO ---");
        boolean canInstallDev1 = service.checkForInstall("PhonePe", dev1);
        System.out.println("Can DEV_ALPHA install PhonePe? " + canInstallDev1);

        if (canInstallDev1) {
            service.executeTask(TaskType.INSTALL, "PhonePe", dev1, null, "1.0.0");
        }

        // 5. Beta Rollout Strategy for Version 2.0.0
        System.out.println("\n--- BETA ROLLOUT SCENARIO ---");
        BetaRolloutStrategy betaStrategy = new BetaRolloutStrategy(Set.of("DEV_ALPHA"));
        service.releaseVersion("PhonePe", "2.0.0", betaStrategy);

        // 6. Check Updates for DEV_ALPHA (In Beta) vs DEV_BETA (Not in Beta / Low OS)
        System.out.println("\n--- CHECK FOR UPDATES ---");
        Optional<AppVersion> updateForDev1 = service.checkForUpdates("PhonePe", "1.0.0", dev1);
        System.out.println("Update available for DEV_ALPHA: " + updateForDev1.map(AppVersion::getVersionId).orElse("None"));

        Optional<AppVersion> updateForDev2 = service.checkForUpdates("PhonePe", "1.0.0", dev2);
        System.out.println("Update available for DEV_BETA: " + updateForDev2.map(AppVersion::getVersionId).orElse("None"));

        // 7. Execute Update Task for DEV_ALPHA
        if (updateForDev1.isPresent()) {
            System.out.println("\n--- EXECUTING UPDATE TASK ---");
            service.executeTask(TaskType.UPDATE, "PhonePe", dev1, "1.0.0", "2.0.0");
        }
    }
}