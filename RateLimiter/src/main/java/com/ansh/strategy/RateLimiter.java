package com.ansh.strategy;

import com.ansh.entity.RateLimitConfig;
import com.ansh.enums.RateLimitType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class RateLimiter {
    protected final RateLimitConfig config;
    protected final RateLimitType type;

    public abstract boolean allowRequest(String userId);
}
