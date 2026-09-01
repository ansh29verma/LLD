package com.ansh.model;

import com.ansh.enums.ChannelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Provider {
    private String id;
    private String name;
    private boolean active;
    private AuthCredentials credentials;
    private Map<ChannelType, String> endpoints; // Channel -> Endpoint URL
    private Map<String, Account> accounts;     // AccountID -> Account
}