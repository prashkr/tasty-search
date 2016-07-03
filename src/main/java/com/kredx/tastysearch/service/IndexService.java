package com.kredx.tastysearch.service;

import com.kredx.tastysearch.review.Review;
import com.kredx.tastysearch.review.ReviewCollection;

import java.util.*;

/**
 * Created by prashkr on 7/3/16.
 */
public class IndexService {
    /**
     * This is our main index. It contains mapping from word to list of reviews containing that word.
     */
    public static Map<String, List<Review>> index = new HashMap<>();

    /**
     * Adds review to index
     *
     * @param word
     * @param review
     */
    public void addReview(String word, Review review) {
        if (index.containsKey(word)) {
            List<Review> reviews = index.get(word);
            reviews.add(review);
        } else {
            List<Review> reviews = new ArrayList<>();
            reviews.add(review);
            index.put(word, reviews);
        }
    }

    /**
     * Fetches review from index
     *
     * @param word
     * @return
     */
    public List<Review> getReview(String word) {
        if (!index.containsKey(word)) {
            return new ArrayList<>();
        }
        return index.get(word);
    }

    /**
     * Populates index from review collection
     */
    public void generateIndex() {
        for (Review review : ReviewCollection.sampledReviews) {
            String reviewText = review.getText();

            StringTokenizer st = new StringTokenizer(reviewText);

            Map<String, Boolean> helperMap = new HashMap<>();
            while (st.hasMoreTokens()) {
                String token = st.nextToken().trim();

                if (!helperMap.containsKey(token)) {
                    addReview(token, review);
                    helperMap.put(token, true);
                }
            }
        }

        System.out.println("Index Generated!");
    }

}