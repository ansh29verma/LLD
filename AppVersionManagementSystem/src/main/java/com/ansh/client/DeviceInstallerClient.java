package com.ansh.client;

import com.ansh.entity.Device;

public interface DeviceInstallerClient {
    void installApp(Device device, byte[] appFile);
    void updateApp(Device device, byte[] diffPack);
}
