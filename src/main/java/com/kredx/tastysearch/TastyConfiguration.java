package com.kredx.tastysearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.dropwizard.Configuration;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by prashkr on 7/3/16.
 */
@Getter
@Setter
public class TastyConfiguration extends Configuration {
    @JsonIgnoreProperties
    private String fileName;

    @JsonIgnoreProperties
    private int sampleSize;

    @JsonIgnoreProperties
    private int indexType;

    @JsonIgnoreProperties
    private int resultSize;

    @JsonIgnoreProperties
    private int testSetSize;

    @JsonIgnoreProperties
    private int maxTestTokens;

    @JsonIgnoreProperties
    private String testSetFileName;
}
