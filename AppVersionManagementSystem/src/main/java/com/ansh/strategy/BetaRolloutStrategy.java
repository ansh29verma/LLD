package com.ansh.strategy;


import com.ansh.entity.Device;

import java.util.Set;

public class BetaRolloutStrategy implements RolloutStrategy {
    private final Set<String> allowedDeviceIds;

    public BetaRolloutStrategy(Set<String> allowedDeviceIds) {
        this.allowedDeviceIds = allowedDeviceIds;
    }

    @Override
    public boolean isEligible(Device device) {
        return allowedDeviceIds != null && allowedDeviceIds.contains(device.getDeviceId());
    }
}
