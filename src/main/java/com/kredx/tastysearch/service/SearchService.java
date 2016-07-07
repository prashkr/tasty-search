package com.kredx.tastysearch.service;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.kredx.tastysearch.dto.ReviewRestDto;
import com.kredx.tastysearch.index.Index;
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
     * @param index
     * @param metricRegistry
     * @return
     */
    public static List<ReviewRestDto> searchUsingIndex(Set<String> queryTokens, int resultSize, Index index, MetricRegistry metricRegistry) {
        int queryLength = queryTokens.size();
        HashMap<Integer, Float> reviewsScoreMap = getReviewsScoreMap(queryTokens, queryLength, index);

        Timer timer = metricRegistry.timer("getTopReviews.time");
        Timer.Context context = timer.time();
        List<ReviewRestDto> topReviews = getTopReviews(reviewsScoreMap, resultSize);
        context.stop();
        return topReviews;
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
     * @param queryTokens
     * @param queryLength
     * @param index
     * @return
     */
    private static HashMap<Integer, Float> getReviewsScoreMap(Set<String> queryTokens, int queryLength, Index index) {
        HashMap<Integer, Float> reviewsCountMap = new HashMap<>();

        for (String queryToken : queryTokens) {
            // fetching matched reviews from query token
            List<Integer> reviews = index.searchIndex(queryToken);

            for (Integer reviewIndex : reviews) {
                if (reviewsCountMap.containsKey(reviewIndex)) {
                    Float reviewMatchCount = reviewsCountMap.get(reviewIndex);
                    reviewsCountMap.put(reviewIndex, reviewMatchCount + 1.f);
                } else {
                    reviewsCountMap.put(reviewIndex, 1.f);
                }
            }
        }

        // Normalizing by query length
        reviewsCountMap.replaceAll((k, v) -> (v / queryLength));
        return reviewsCountMap;
    }

//    /**
//     * Fetches top reviews. Steps:
//     *
//     * 1. Sorts reviews by score
//     * 2. Picks specified number of top scoring reviews.
//     *
//     * @param reviewsScoreMap
//     * @param resultSize
//     * @return
//     */
//    private static List<ReviewRestDto> getTopReviews(Map<Integer, Float> reviewsScoreMap, int resultSize) {
//        List<ReviewRestDto> reviewRestDtoList = new ArrayList<>();
//
//        // Sorting reviews by score
//        List<Pair> sortedReviewList = getReviewsSortedByScore(reviewsScoreMap);
//
//        //picking top reviews
//        for (int i = 0; i < Math.min(resultSize, sortedReviewList.size()); i++) {
//            Pair pair = sortedReviewList.get(i);
//            int reviewIndex = pair.getReviewIndex();
//            float searchScore = pair.getSearchScore();
//
//            ReviewRestDto reviewRestDto = new ReviewRestDto(ReviewCollection.get(reviewIndex), searchScore);
//            reviewRestDtoList.add(reviewRestDto);
//        }
//
//        return reviewRestDtoList;
//    }
//
//        /**
//     *
//     * @param reviewsCountMap
//     * @return
//     */
//    private static List<Pair> getReviewsSortedByScore(Map<Integer, Float> reviewsCountMap) {
//        List<Pair> reviewList = new ArrayList<>();
//
//        reviewsCountMap.forEach((reviewIndex, score) -> {
//            Pair p = new Pair(reviewIndex, score);
//            reviewList.add(p);
//        });
//
//        reviewList.sort((o1, o2) -> {
//            Review review1 = ReviewCollection.get(o1.getReviewIndex());
//            Review review2 = ReviewCollection.get(o2.getReviewIndex());
//            int compare = Float.compare(o2.getSearchScore(), o1.getSearchScore());
//
//            if (compare == 0) {
//                // Breaking ties if search scores are equal
//                return Float.compare(review2.getScore(), review1.getScore());
//            }
//
//            return compare;
//        });
//
//        return reviewList;
//    }

    /**
     * Fetches top reviews. Steps:
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

        // get top scoring reviews in heap data structure
        PriorityQueue<Pair> topReviewsByScore = getTopReviewsInHeapStructure(reviewsScoreMap, resultSize);

        // put top reviews into dto for transfer
        for (int i = 0; i < Math.min(resultSize, topReviewsByScore.size()); i++) {
            Pair review = topReviewsByScore.poll();
            int reviewIndex = review.getReviewIndex();
            float searchScore = review.getSearchScore();

            ReviewRestDto reviewRestDto = new ReviewRestDto(ReviewCollection.get(reviewIndex), searchScore);
            reviewRestDtoList.add(reviewRestDto);
        }

        // finally sort in descending order
        reviewRestDtoList.sort((o1, o2) -> Float.compare(o2.getSearchScore(), o1.getSearchScore()));
        return reviewRestDtoList;
    }

    /**
     *
     *
     * @param reviewsScoreMap
     * @param resultSize
     * @return
     */
    private static PriorityQueue<Pair> getTopReviewsInHeapStructure(Map<Integer, Float> reviewsScoreMap, int resultSize) {
        // working as a min heap
        PriorityQueue<Pair> topReviewsByScore;
        topReviewsByScore = new PriorityQueue<>(resultSize, (o1, o2) -> Float.compare(o1.getSearchScore(), o2.getSearchScore()));

        int count = 1;
        for (Map.Entry<Integer, Float> entry : reviewsScoreMap.entrySet()) {
            Integer currentReviewIndex = entry.getKey();
            Float currentReviewScore = entry.getValue();

            Pair currentPair = new Pair(currentReviewIndex, currentReviewScore);

            if (count > resultSize) {
                // get min scored review from the heap
                Pair minScoredReviewPair = topReviewsByScore.peek();
                float minScoredReviewScore = minScoredReviewPair.getSearchScore();


                if (currentReviewScore == minScoredReviewScore) {
                    // resolve ties based on score in the review
                    Review currentReview = ReviewCollection.get(currentReviewIndex);
                    Review minScoredReview = ReviewCollection.get(minScoredReviewPair.getReviewIndex());

                    if (currentReview.getScore() > minScoredReview.getScore()) {
                        topReviewsByScore.remove(minScoredReviewPair);
                        topReviewsByScore.add(currentPair);
                    }
                } else if (currentReviewScore > minScoredReviewScore) {
                    // if current review has greater score than the minimum in the heap
                    // then remove the min scored review from the heap and add the current
                    // review.
                    topReviewsByScore.remove(minScoredReviewPair);
                    topReviewsByScore.add(currentPair);
                }
            } else {
                // filling first 'resultSize' reviews into the heap
                topReviewsByScore.add(currentPair);
            }
            count++;
        }
        return topReviewsByScore;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pair pair = (Pair) o;

            if (reviewIndex != pair.reviewIndex) return false;
            return Float.compare(pair.searchScore, searchScore) == 0;

        }

        @Override
        public int hashCode() {
            int result = reviewIndex;
            result = 31 * result + (searchScore != +0.0f ? Float.floatToIntBits(searchScore) : 0);
            return result;
        }
    }
}
