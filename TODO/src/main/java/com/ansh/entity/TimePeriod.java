package com.ansh.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TimePeriod {

    private final LocalDateTime from;

    private final LocalDateTime to;

    public boolean contains(LocalDateTime time) {

        if (time == null) {

            return false;
        }

        boolean afterFrom =
                from == null || !time.isBefore(from);

        boolean beforeTo =
                to == null || !time.isAfter(to);

        return afterFrom && beforeTo;
    }
}
