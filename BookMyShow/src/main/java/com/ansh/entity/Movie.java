package com.ansh.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Movie {
    private final String id;
    private final String title;
    private final int durationInMinutes;

}
