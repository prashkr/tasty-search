package com.kredx.tastysearch.service;

import java.util.Random;

/**
 * Created by prashkr on 7/4/16.
 */
public class RandomNumberService {
    private static Random rand = new Random();

    /**
     * Generates random integer between [min, max]
     *
     * @param min lower bound
     * @param max upper bound
     * @return
     */
    public static int generateRandomNumberBetween(int min, int max) {
        return min + rand.nextInt(1 + (max - min));
    }
}
