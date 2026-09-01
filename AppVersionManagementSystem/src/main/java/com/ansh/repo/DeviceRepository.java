package com.ansh.repo;

import com.ansh.entity.Device;

import java.util.Optional;

public interface DeviceRepository {
    Device save(Device device);
    Optional<Device> findById(String deviceId);
}
