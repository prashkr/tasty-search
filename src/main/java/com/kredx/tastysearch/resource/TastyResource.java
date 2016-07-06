package com.kredx.tastysearch.resource;

import com.codahale.metrics.MetricRegistry;
import com.kredx.tastysearch.TastyConfiguration;
import com.kredx.tastysearch.dto.ReviewRestDto;
import com.kredx.tastysearch.index.Index;
import com.kredx.tastysearch.service.FilterService;
import com.kredx.tastysearch.service.LoadTestingService;
import com.kredx.tastysearch.service.SearchService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by prashkr on 7/3/16.
 */
@Path("v1")
public class TastyResource {

    private final TastyConfiguration configuration;
    private final Index index;
    private final MetricRegistry metricRegistry;

    public TastyResource(TastyConfiguration tastyConfiguration, Index index, MetricRegistry metrics) {
        this.configuration = tastyConfiguration;
        this.index = index;
        this.metricRegistry = metrics;
    }

    @GET
    public String getTaste() {
        return "Suuweeet!!";
    }

    /**
     * Get results using index. Steps:
     * 1. Splits input into list of unique tokens
     * 2. Filters each token
     * 3. Searches using index and returns top matching reviews
     *
     * @param query e.g "good bad ugly"
     * @return
     */
    @Path("search-index")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReviewRestDto> searchReviewsUsingIndex(String query) {
        String[] tokens = query.trim().split(" ");
        Set<String> filteredQuerySet = FilterService.filter(Arrays.asList(tokens));

        return SearchService.searchUsingIndex(filteredQuerySet, configuration.getResultSize(), index, metricRegistry);
    }

    /**
     * Get results WITHOUT using index i.e plain brute force approach. Steps:
     * 1. Splits input into list of unique tokens
     * 2. Filters each token
     * 3. Searches WITHOUT using index and returns top matching reviews
     *
     * @param query
     * @return
     */
    @Path("search")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReviewRestDto> searchReviewsWithoutIndex(String query) {
        String[] tokens = query.trim().split(" ");
        Set<String> filteredQuerySet = FilterService.filter(Arrays.asList(tokens));

        return SearchService.searchWithoutIndex(filteredQuerySet, configuration.getResultSize());
    }

    /**
     *
     *
     * @return
     */
    @Path("generate-query-data")
    @GET
    public String generateQueryData() {
        try {
            LoadTestingService.generateTestSet(configuration.getTestSetFileName(),
                    configuration.getTestSetSize(), configuration.getMaxTestTokens());
            return "Data generated";
        } catch (IOException e) {
            return "IOException!!";
        }
    }
}