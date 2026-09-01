package com.ansh.entity;


import com.ansh.enums.OsType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppVersion implements Comparable<AppVersion> {
    private String versionId; // e.g., "1.0.0", "1.1.0"
    private String appId;
    private OsType osType;
    private int minOsVersion;
    private String fileUrl;
    private LocalDateTime releaseDate;

    @Override
    public int compareTo(AppVersion other) {
        String[] v1 = this.versionId.split("\\.");
        String[] v2 = other.versionId.split("\\.");
        int length = Math.max(v1.length, v2.length);

        for (int i = 0; i < length; i++) {
            int num1 = i < v1.length ? Integer.parseInt(v1[i]) : 0;
            int num2 = i < v2.length ? Integer.parseInt(v2[i]) : 0;
            if (num1 != num2) {
                return Integer.compare(num1, num2);
            }
        }
        return 0;
    }
}
