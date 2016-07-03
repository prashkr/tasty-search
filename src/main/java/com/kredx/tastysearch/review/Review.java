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
}
