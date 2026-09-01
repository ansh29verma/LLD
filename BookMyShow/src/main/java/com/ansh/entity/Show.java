package com.ansh.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Show {
    private final String id;
    private final Movie movie;
    private final String startTimeStr;
    private final Theater theater;
    private final Screen screen;

}
