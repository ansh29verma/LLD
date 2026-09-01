package com.ansh.model;


import com.ansh.enums.ChannelType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private String accountId;
    private String accountName;
    private Set<ChannelType> supportedChannels;
    private boolean active;
}