package com.kredx.tastysearch.resource;

import com.kredx.tastysearch.TastyConfiguration;
import com.kredx.tastysearch.dto.ReviewRestDto;
import com.kredx.tastysearch.service.FilterService;
import com.kredx.tastysearch.service.IndexService;
import com.kredx.tastysearch.service.LoadService;

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

    public TastyResource(TastyConfiguration tastyConfiguration) {
        configuration = tastyConfiguration;
    }

    @GET
    public String getTaste() {
        return "Suuweeet!!";
    }

    @Path("search")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReviewRestDto> searchReviews(String query) {
        String[] tokens = query.trim().split(" ");
        Set<String> filteredQuerySet = FilterService.filter(Arrays.asList(tokens));
        return IndexService.search(filteredQuerySet, configuration.getResultSize());
    }

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