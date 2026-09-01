package com.ansh.repo;


import com.ansh.entity.Device;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDeviceRepository implements DeviceRepository {
    private final Map<String, Device> storage = new ConcurrentHashMap<>();

    @Override
    public Device save(Device device) {
        storage.put(device.getDeviceId(), device);
        return device;
    }

    @Override
    public Optional<Device> findById(String deviceId) {
        return Optional.ofNullable(storage.get(deviceId));
    }
}
