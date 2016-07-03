package com.kredx.tastysearch.resources;

import com.kredx.tastysearch.review.Review;
import com.kredx.tastysearch.review.ReviewCollection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by prashkr on 7/3/16.
 */
@Path("v1")
public class SearchResource {

    @GET
    public String ping() {
        return String.format("Hey there, %s. You know the secret!", "Prashant");
    }

    @Path("reviews")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Review> showReviews() {
        return ReviewCollection.sampledReviews.subList(0, 10);
    }
}