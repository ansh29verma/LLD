package com.ansh.entity;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Fare {
    private final String id;
    private final Product product;
    private final Location source;
    private final Location destination;
    private final double amount;
    @Builder.Default
    private final long createdAt = System.currentTimeMillis();
}
