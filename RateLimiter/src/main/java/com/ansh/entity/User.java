package com.ansh.entity;

import com.ansh.enums.UserTier;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {
    private final String userId;
    private final UserTier tier;
}
