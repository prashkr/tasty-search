package com.kredx.tastysearch.resources;

import com.kredx.tastysearch.TastySearchConfiguration;
import com.kredx.tastysearch.dto.ReviewRestDto;
import com.kredx.tastysearch.review.Review;
import com.kredx.tastysearch.review.ReviewCollection;
import com.kredx.tastysearch.service.FilterService;
import com.kredx.tastysearch.service.IndexService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Set;

/**
 * Created by prashkr on 7/3/16.
 */
@Path("v1")
public class SearchResource {

    private final TastySearchConfiguration configuration;

    public SearchResource(TastySearchConfiguration tastySearchConfiguration) {
        configuration = tastySearchConfiguration;
    }

    @GET
    public String ping() {
        return "pong";
    }

    @Path("reviews")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Review> showReviews() {
        return ReviewCollection.sampledReviews.subList(0, 10);
    }

    @Path("search")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReviewRestDto> searchReviews(List<String> queryTokens) {
        Set<String> filteredQuerySet = FilterService.filter(queryTokens);
        return IndexService.search(filteredQuerySet, configuration.getResultSize());
    }
}