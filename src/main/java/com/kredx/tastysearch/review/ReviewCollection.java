package com.kredx.tastysearch.review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prashkr on 7/3/16.
 */
public class ReviewCollection {
    public static List<Review> sampledReviews = new ArrayList<>();

    /**
     *
     * @param review
     */
    public static void add(Review review) {
        sampledReviews.add(review);
    }

    /**
     * Updates review at the given index. Used for reservoir sampling.
     *
     * @param index
     * @param review
     */
    public static void update(int index, Review review) {
        sampledReviews.set(index, review);
    }
}
