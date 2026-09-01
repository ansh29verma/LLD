package com.ansh.strategy;


import com.ansh.entity.Device;

public class PercentageRolloutStrategy implements RolloutStrategy {
    private final int percentage; // 0 to 100

    public PercentageRolloutStrategy(int percentage) {
        this.percentage = Math.min(100, Math.max(0, percentage));
    }

    @Override
    public boolean isEligible(Device device) {
        int hash = Math.abs(device.getDeviceId().hashCode());
        return (hash % 100) < percentage;
    }
}
