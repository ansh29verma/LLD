package com.ansh.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserTier {
    PLATINUM(10),
    GOLD(5),
    SILVER(3);

    private final int maxBookingLimit;
}
