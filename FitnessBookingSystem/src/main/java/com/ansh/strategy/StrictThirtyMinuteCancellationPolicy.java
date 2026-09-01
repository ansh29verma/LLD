package com.ansh.strategy;

import java.time.Duration;
import java.time.LocalDateTime;

public class StrictThirtyMinuteCancellationPolicy implements CancellationPolicy {
    @Override
    public boolean isCancellationAllowed(LocalDateTime classStartTime, LocalDateTime cancellationTime) {
        Duration duration = Duration.between(cancellationTime, classStartTime);
        return duration.toMinutes() >= 30;
    }
}
