package com.ansh.dto;


import com.ansh.entity.FitnessClass;
import com.ansh.enums.ClassType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FitnessClassResponseDto {
    private String classId;
    private String title;
    private ClassType classType;
    private int capacity;
    private int confirmedBookingsCount;
    private int waitlistCount;
    private LocalDateTime startTime;
    private boolean isCancelled;

    public static FitnessClassResponseDto fromEntity(FitnessClass fc) {
        synchronized (fc) {
            return FitnessClassResponseDto.builder()
                    .classId(fc.getClassId())
                    .title(fc.getTitle())
                    .classType(fc.getClassType())
                    .capacity(fc.getCapacity())
                    .confirmedBookingsCount(fc.getConfirmedUserIds().size())
                    .waitlistCount(fc.getWaitlistedUserIds().size())
                    .startTime(fc.getStartTime())
                    .isCancelled(fc.isCancelled())
                    .build();
        }
    }
}
