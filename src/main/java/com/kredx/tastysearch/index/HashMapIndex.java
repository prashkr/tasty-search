package com.kredx.tastysearch.index;

import com.kredx.tastysearch.review.Review;
import com.kredx.tastysearch.review.ReviewCollection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

/**
 * Created by prashkr on 7/6/16.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HashMapIndex implements Index {
    /**
     * This is our hash map index.
     * It contains mapping from word to list of reviews containing that word.
     */
    public Map<String, List<Integer>> index = new HashMap<>();

    /**
     * Populates index from review collection.
     * Gets tokens from each review and adds it to the index.
     */
    @Override
    public void buildIndex() {
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

    @Override
    public List<Integer> searchIndex(String queryToken) {
        if (!index.containsKey(queryToken)) {
            return new ArrayList<>();
        }

        return index.get(queryToken);
    }
}
