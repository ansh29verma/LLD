package com.ansh.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UserPreference {
    private final String userId;
    private final Set<ChannelType> preferredChannels;
}
