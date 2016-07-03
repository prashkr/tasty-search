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

    /**
     *
     *
     * @param queryTokens
     * @param resultSize
     * @return
     */
    public static List<ReviewRestDto> search(Set<String> queryTokens, int resultSize) {
        int queryLength = queryTokens.size();
        HashMap<Integer, Float> reviewsCountMap = getReviewsCountMap(queryTokens);
        return getTopReviews(reviewsCountMap, queryLength, resultSize);
    }

    /**
     *
     * @param queryTokens
     * @return
     */
    private static HashMap<Integer, Float> getReviewsCountMap(Set<String> queryTokens) {
        HashMap<Integer, Float> reviewsCountMap = new HashMap<>();

        for (String queryToken : queryTokens) {

            if (index.containsKey(queryToken)) {
                List<Integer> reviews = index.get(queryToken);

                for (Integer reviewIndex : reviews) {
                    if (reviewsCountMap.containsKey(reviewIndex)) {
                        Float reviewMatchCount = reviewsCountMap.get(reviewIndex);
                        reviewsCountMap.put(reviewIndex, reviewMatchCount + 1.f);
                    } else {
                        reviewsCountMap.put(reviewIndex, 1.f);
                    }
                }
            }
        }
        return reviewsCountMap;
    }

    /**
     * Fetches top reviews in three steps:
     *
     * 1. Normalizes total scores by query length
     * 2. Sorts reviews by score
     * 3. Picks specified number of top scoring reviews.
     *
     * @param reviewsCountMap
     * @param queryLength
     * @param resultSize
     * @return
     */
    private static List<ReviewRestDto> getTopReviews(HashMap<Integer, Float> reviewsCountMap, int queryLength, int resultSize) {
        List<ReviewRestDto> reviewRestDtoList = new ArrayList<>();

        // Normalizing by query length
        reviewsCountMap.replaceAll((k, v) -> (v / queryLength));

        // Sorting reviews by score
        List<Pair> sortedReviewList = getReviewsSortedByScore(reviewsCountMap);

        //picking top reviews
        for (int i = 0; i < Math.min(resultSize, sortedReviewList.size()); i++) {
            Pair pair = sortedReviewList.get(i);
            int reviewIndex = pair.getReviewIndex();
            float searchScore = pair.getSearchScore();

            ReviewRestDto reviewRestDto = new ReviewRestDto(ReviewCollection.get(reviewIndex), searchScore);
            reviewRestDtoList.add(reviewRestDto);
        }

        return reviewRestDtoList;
    }

    /**
     *
     * @param reviewsCountMap
     * @return
     */
    private static List<Pair> getReviewsSortedByScore(HashMap<Integer, Float> reviewsCountMap) {
        List<Pair> reviewList = new ArrayList<>();

        reviewsCountMap.forEach((reviewIndex, score) -> {
            Pair p = new Pair(reviewIndex, score);
            reviewList.add(p);
        });

        reviewList.sort((o1, o2) -> {
            Review review1 = ReviewCollection.get(o1.getReviewIndex());
            Review review2 = ReviewCollection.get(o2.getReviewIndex());
            int compare = Float.compare(o2.getSearchScore(), o1.getSearchScore());

            if (compare == 0) {
                // Breaking ties if search scores are equal
                return Float.compare(review2.getScore(), review1.getScore());
            }

            return compare;
        });

        return reviewList;
    }

    @AllArgsConstructor
    @Getter
    public static class Pair {
        int reviewIndex;
        float searchScore;
    }
}