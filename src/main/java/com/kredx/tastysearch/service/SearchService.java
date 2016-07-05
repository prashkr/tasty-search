package com.kredx.tastysearch.service;

import com.kredx.tastysearch.dto.ReviewRestDto;
import com.kredx.tastysearch.review.Review;
import com.kredx.tastysearch.review.ReviewCollection;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

/**
 * Created by prashkr on 7/5/16.
 */
public class SearchService {

    /**
     *
     *
     * @param queryTokens
     * @param resultSize
     * @return
     */
    public static List<ReviewRestDto> searchUsingIndex(Set<String> queryTokens, int resultSize) {
        int queryLength = queryTokens.size();
        HashMap<Integer, Float> reviewsScoreMap = getReviewsScoreMap(queryTokens, queryLength);
        return getTopReviews(reviewsScoreMap, resultSize);
    }

    /**
     *
     * @param queryTokens
     * @param queryLength
     * @return
     */
    private static HashMap<Integer, Float> getReviewsScoreMap(Set<String> queryTokens, int queryLength) {
        HashMap<Integer, Float> reviewsCountMap = new HashMap<>();

        for (String queryToken : queryTokens) {

            if (IndexService.index.containsKey(queryToken)) {
                List<Integer> reviews = IndexService.index.get(queryToken);

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
        // Normalizing by query length
        reviewsCountMap.replaceAll((k, v) -> (v / queryLength));

        return reviewsCountMap;
    }

    /**
     * Fetches top reviews in three steps:
     *
     * 1. Sorts reviews by score
     * 2. Picks specified number of top scoring reviews.
     *
     * @param reviewsScoreMap
     * @param resultSize
     * @return
     */
    private static List<ReviewRestDto> getTopReviews(Map<Integer, Float> reviewsScoreMap, int resultSize) {
        List<ReviewRestDto> reviewRestDtoList = new ArrayList<>();

        // Sorting reviews by score
        List<Pair> sortedReviewList = getReviewsSortedByScore(reviewsScoreMap);

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
    private static List<Pair> getReviewsSortedByScore(Map<Integer, Float> reviewsCountMap) {
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

    /**
     *
     *
     * @param filteredQuerySet
     * @param resultSize
     * @return
     */
    public static List<ReviewRestDto> searchWithoutIndex(Set<String> filteredQuerySet, int resultSize) {
        Map<Integer, Float> reviewsScoreMap = new HashMap<>();
        List<Review> sampledReviews = ReviewCollection.sampledReviews;

        for (int i = 0; i < sampledReviews.size(); i++) {
            Review sampledReview = sampledReviews.get(i);
            String filteredText = sampledReview.getFilteredText();
            float reviewScore = getReviewScore(filteredQuerySet, filteredText);
            if (reviewScore != 0f) {
                reviewsScoreMap.put(i, reviewScore);
            }
        }

        return getTopReviews(reviewsScoreMap, resultSize);
    }

    /**
     *
     *
     * @param filteredQuerySet
     * @param filteredText
     * @return
     */
    private static float getReviewScore(Set<String> filteredQuerySet, String filteredText) {
        float docScore = 0;
        int querySize = filteredQuerySet.size();

        for (String queryToken : filteredQuerySet) {
            if (filteredText.contains(queryToken)) {
                docScore += 1.0f;
            }
        }

        return docScore/querySize;
    }

    @AllArgsConstructor
    @Getter
    public static class Pair {
        int reviewIndex;
        float searchScore;
    }
}
