package com.kredx.tastysearch.parser;

import com.kredx.tastysearch.service.FilterService;
import com.kredx.tastysearch.review.Review;
import lombok.AllArgsConstructor;

/**
 * Created by prashkr on 7/3/16.
 */
@AllArgsConstructor
public class ReviewParser {

    /**
     * Converts a review string into {@link Review} object.
     *
     * @param reviewString Review in string form
     * @return Review object
     */
    public static Review parse(String reviewString) {
        Review review = new Review();

        String[] lines = reviewString.split("\n");
        for (String line : lines) {
            String[] keyValue = line.split(": ");
            String key = keyValue[0];
            try {
                String value = keyValue[1];
                setReviewFields(review, key, value);
            } catch (Exception e) {
                System.out.println("Error parsing a review");
            }
        }

        return review;
    }

    /**
     * Helper function for setting review fields
     *
     * @param review Empty review object
     * @param key Helps to decide which field to fill
     * @param value The value of field
     */
    private static void setReviewFields(Review review, String key, String value) {
        if (key.contains("productId")) {
            review.setProductId(value);

        } else if (key.contains("userId")) {
            review.setUserId(value);

        } else if (key.contains("profileName")) {
            review.setProfileName(value);

        } else if (key.contains("helpfulness")) {
            review.setHelpfulness(value);

        } else if (key.contains("score")) {
            review.setScore(Float.valueOf(value));

        } else if (key.contains("time")) {
            review.setTime(Long.valueOf(value));

        } else if (key.contains("summary")) {
            review.setSummary(value);

        } else if (key.contains("text")) {
            String filteredText = FilterService.filter(value);
            review.setFilteredText(filteredText);
            review.setOriginalText(value);
        }
    }
}
