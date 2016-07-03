package com.kredx.tastysearch.service;

import com.kredx.tastysearch.review.Review;
import com.kredx.tastysearch.review.ReviewCollection;

import java.util.*;

/**
 * Created by prashkr on 7/4/16.
 */
public class LoadService {
    /**
     * List of all the words in all the reviews. No duplicates here.
     * Todo: Remove stop words such as 'a', 'of', 'the', 'if', etc.
     *
     */
    public static List<String> words = new ArrayList<>();

    /**
     * Loads words from all reviews into the above mentioned structure.
     */
    public void loadWords() {
        Map<String, Boolean> wordMap = new HashMap<>();

        for (Review review : ReviewCollection.sampledReviews) {
            String reviewText = review.getFilteredText();
            StringTokenizer st = new StringTokenizer(reviewText);

            while (st.hasMoreTokens()) {
                String token = st.nextToken().trim();

                if (!wordMap.containsKey(token)) {
                    //Todo: exclude stop words
                    wordMap.put(token, true);
                }
            }
        }

        words.addAll(wordMap.keySet());
    }

    public static void generateQueryData(int querySize, int maxQueryTokens) {

    }
}
