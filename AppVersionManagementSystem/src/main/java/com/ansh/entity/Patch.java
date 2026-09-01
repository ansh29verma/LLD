package com.ansh.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Patch {
    private String patchId;
    private String appId;
    private String fromVersion;
    private String toVersion;
    private String patchUrl;
}
