package com.kredx.tastysearch.index;

import com.googlecode.concurrenttrees.radix.ConcurrentRadixTree;
import com.googlecode.concurrenttrees.radix.RadixTree;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import com.kredx.tastysearch.review.Review;
import com.kredx.tastysearch.review.ReviewCollection;

import java.util.*;

/**
 * Created by prashkr on 7/6/16.
 */
public class RadixTreeIndex implements Index{
    /**
     * This is our radix tree or a compressed trie index.
     */
    public RadixTree<List<Integer>> index = new ConcurrentRadixTree<>(new DefaultCharArrayNodeFactory());

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
    }

    private void addReview(String word, int reviewIndex) {
        List<Integer> oldReviews = index.getValueForExactKey(word);

        if (oldReviews != null) {
            oldReviews.add(reviewIndex);
        } else {
            List<Integer> reviews = new ArrayList<>();
            reviews.add(reviewIndex);
            index.put(word, reviews);
        }
    }

    @Override
    public List<Integer> searchIndex(String query) {
        List<Integer> reviews = index.getValueForExactKey(query);
        if (reviews == null) {
            return new ArrayList<>();
        }
        return reviews;
    }
}
