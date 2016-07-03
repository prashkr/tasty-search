package com.kredx.tastysearch.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by prashkr on 7/2/16.
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Review {
    private String productId;
    private String userId;
    private String profileName;
    private String helpfulness;
    private float score;
    private long time;
    private String summary;
    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;

        if (Float.compare(review.score, score) != 0) return false;
        if (time != review.time) return false;
        if (productId != null ? !productId.equals(review.productId) : review.productId != null) return false;
        if (userId != null ? !userId.equals(review.userId) : review.userId != null) return false;
        if (profileName != null ? !profileName.equals(review.profileName) : review.profileName != null) return false;
        if (helpfulness != null ? !helpfulness.equals(review.helpfulness) : review.helpfulness != null) return false;
        if (summary != null ? !summary.equals(review.summary) : review.summary != null) return false;
        return text.equals(review.text);

    }

    @Override
    public int hashCode() {
        int result = productId != null ? productId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (profileName != null ? profileName.hashCode() : 0);
        result = 31 * result + (helpfulness != null ? helpfulness.hashCode() : 0);
        result = 31 * result + (score != +0.0f ? Float.floatToIntBits(score) : 0);
        result = 31 * result + (int) (time ^ (time >>> 32));
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + text.hashCode();
        return result;
    }
}
