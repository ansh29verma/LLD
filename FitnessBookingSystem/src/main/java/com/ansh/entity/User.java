package com.ansh.entity;


import com.ansh.enums.UserTier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String userId;
    private String name;
    private String email;
    private UserTier tier;

    @Builder.Default
    private AtomicInteger activeBookingsCount = new AtomicInteger(0);

    public int getActiveBookingsCount() {
        return activeBookingsCount.get();
    }

    public boolean incrementBookingIfPermitted() {
        while (true) {
            int current = activeBookingsCount.get();
            if (current >= tier.getMaxBookingLimit()) {
                return false;
            }
            if (activeBookingsCount.compareAndSet(current, current + 1)) {
                return true;
            }
        }
    }

    public void decrementBookingCount() {
        activeBookingsCount.decrementAndGet();
    }
}