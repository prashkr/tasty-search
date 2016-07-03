package com.kredx.tastysearch;

import com.kredx.tastysearch.service.IndexService;
import com.kredx.tastysearch.resource.TastyResource;
import com.kredx.tastysearch.parser.FileParser;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by prashkr on 7/3/16.
 */
public class TastyApplication extends Application<TastyConfiguration> {
    public static void main(String[] args) throws Exception {
        new TastyApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<TastyConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html"));
    }

    @Override
    public void run(TastyConfiguration configuration,
                    Environment environment) throws ClassNotFoundException {
        environment.jersey().register(new TastyResource(configuration));

        new FileParser(configuration).parse();
        new IndexService().generateIndex();
    }
}
