package com.kredx.tastysearch;

import com.kredx.tastysearch.service.IndexService;
import com.kredx.tastysearch.resources.SearchResource;
import com.kredx.tastysearch.parser.FileParser;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by prashkr on 7/3/16.
 */
public class TastySearchApplication extends Application<TastySearchConfiguration> {
    public static void main(String[] args) throws Exception {
        new TastySearchApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<TastySearchConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html"));
    }

    @Override
    public void run(TastySearchConfiguration configuration,
                    Environment environment) throws ClassNotFoundException {
        environment.jersey().register(new SearchResource(configuration));

        new FileParser(configuration).parse();
        new IndexService().generateIndex();
    }
}
