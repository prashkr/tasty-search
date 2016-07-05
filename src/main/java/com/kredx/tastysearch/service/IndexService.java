package com.kredx.tastysearch.service;

import com.kredx.tastysearch.dto.ReviewRestDto;
import com.kredx.tastysearch.review.Review;
import com.kredx.tastysearch.review.ReviewCollection;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

/**
 * Created by prashkr on 7/3/16.
 */
public class IndexService {
    /**
     * This is our main index. It contains mapping from word to list of reviews containing that word.
     */
    public static Map<String, List<Integer>> index = new HashMap<>();

    /**
     * Adds review index to search_index
     *  @param word
     * @param reviewIndex
     */
    public void addReview(String word, int reviewIndex) {
        if (index.containsKey(word)) {
            List<Integer> reviews = index.get(word);
            reviews.add(reviewIndex);
        } else {
            List<Integer> reviews = new ArrayList<>();
            reviews.add(reviewIndex);
            index.put(word, reviews);
        }
    }

    /**
     * Fetches review indexes from search_index
     *
     * @param word
     * @return
     */
    public List<Integer> getReview(String word) {
        if (!index.containsKey(word)) {
            return new ArrayList<>();
        }
        return index.get(word);
    }

    /**
     * Populates index from review collection.
     * Gets tokens from each review and adds it to the index.
     */
    public void generateIndex() {
        List<Review> sampledReviews = ReviewCollection.sampledReviews;

        for (int i = 0; i < sampledReviews.size(); i++) {
            Review review = sampledReviews.get(i);
            String reviewText = review.getFilteredText();

            StringTokenizer st = new StringTokenizer(reviewText);

            Map<String, Boolean> helperMap = new HashMap<>();
            while (st.hasMoreTokens()) {
                String token = st.nextToken().trim();

                if (!helperMap.containsKey(token)) {
                    addReview(token, i);
                    helperMap.put(token, true);
                }
            }
        }

        System.out.println("Index Generated!");
    }
}