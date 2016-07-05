package com.kredx.tastysearch.resource;

import com.kredx.tastysearch.TastyConfiguration;
import com.kredx.tastysearch.dto.ReviewRestDto;
import com.kredx.tastysearch.index.Index;
import com.kredx.tastysearch.service.FilterService;
import com.kredx.tastysearch.service.LoadService;
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

    public TastyResource(TastyConfiguration tastyConfiguration, Index index) {
        this.configuration = tastyConfiguration;
        this.index = index;
    }

    @GET
    public String getTaste() {
        return "Suuweeet!!";
    }

    /**
     *
     *
     * @param query
     * @return
     */
    @Path("search-index")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReviewRestDto> searchReviewsUsingIndex(String query) {
        String[] tokens = query.trim().split(" ");
        Set<String> filteredQuerySet = FilterService.filter(Arrays.asList(tokens));
        return SearchService.searchUsingIndex(filteredQuerySet, configuration.getResultSize(), index);
    }

    /**
     *
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
            LoadService.generateTestSet(configuration.getTestSetFileName(),
                    configuration.getTestSetSize(), configuration.getMaxTestTokens());
            return "Data generated";
        } catch (IOException e) {
            return "IOException!!";
        }
    }
}