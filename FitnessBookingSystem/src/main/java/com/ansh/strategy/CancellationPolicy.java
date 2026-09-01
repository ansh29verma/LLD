package com.ansh.strategy;

import java.time.LocalDateTime;

public interface CancellationPolicy {
    boolean isCancellationAllowed(LocalDateTime classStartTime, LocalDateTime cancellationTime);
}
