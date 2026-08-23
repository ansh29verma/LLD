package com.ansh;

import java.util.Random;

public class Dice {

    private final int min = 1;
    private final int max = 6;

    private final Random random = new Random();

    public int rollDice() {
        return random.nextInt(min,max+1);
    }
}

