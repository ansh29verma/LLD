package com.ansh.entity;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Location {
    private final double latitude;
    private final double longitude;

    public double distanceTo(Location other) {
        return Math.hypot(this.latitude - other.latitude, this.longitude - other.longitude);
    }
}