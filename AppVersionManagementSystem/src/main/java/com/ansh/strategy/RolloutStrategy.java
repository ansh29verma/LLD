package com.ansh.strategy;

import com.ansh.entity.Device;

public interface RolloutStrategy {
    boolean isEligible(Device device);
}
