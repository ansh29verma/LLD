package com.ansh.entity;

import com.ansh.enums.OsType;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Device {
    private String deviceId;
    private OsType osType;
    private int osVersion; // e.g., Android 13 -> 13
}
