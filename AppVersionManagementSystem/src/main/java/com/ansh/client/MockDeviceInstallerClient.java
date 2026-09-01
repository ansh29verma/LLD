package com.ansh.client;


import com.ansh.entity.Device;

public class MockDeviceInstallerClient implements DeviceInstallerClient {

    @Override
    public void installApp(Device device, byte[] appFile) {
        System.out.println(String.format("[DEVICE: %s] Installed fresh app. Binary payload size: %d bytes.",
                device.getDeviceId(), appFile.length));
    }

    @Override
    public void updateApp(Device device, byte[] diffPack) {
        System.out.println(String.format("[DEVICE: %s] Applied patch update. Patch payload size: %d bytes.",
                device.getDeviceId(), diffPack.length));
    }
}
