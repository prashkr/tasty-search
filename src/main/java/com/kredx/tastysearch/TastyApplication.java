package com.kredx.tastysearch;

import com.kredx.tastysearch.index.HashMapIndex;
import com.kredx.tastysearch.index.Index;
import com.kredx.tastysearch.index.RadixTreeIndex;
import com.kredx.tastysearch.parser.FileParser;
import com.kredx.tastysearch.resource.TastyResource;
import com.kredx.tastysearch.service.LoadTestingService;
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
        Index index = null;
        if (configuration.getIndexType() == 1) {
            index = new HashMapIndex();
        } else if (configuration.getIndexType() == 2){
            index = new RadixTreeIndex();
        }
        assert index != null;

        // parse reviews file to generate samples
        new FileParser(configuration).parse();
        // build index depending on index type
        index.buildIndex();
        // load all words into memory for generating test set later
        new LoadTestingService().loadWords();

        environment.jersey().register(new TastyResource(configuration, index));
    }
}
