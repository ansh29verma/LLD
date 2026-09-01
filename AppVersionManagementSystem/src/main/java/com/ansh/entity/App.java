package com.ansh.entity;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class App {
    private String appId;
    private String name;

    @Builder.Default
    private Map<String, AppVersion> versions = new ConcurrentHashMap<>();
}
