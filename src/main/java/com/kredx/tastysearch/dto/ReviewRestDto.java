package com.kredx.tastysearch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kredx.tastysearch.review.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by prashkr on 7/3/16.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewRestDto {
    @JsonProperty("review")
    private Review review;

    @JsonProperty("score")
    private float searchScore;
}
