package com.sample.app;

import com.sample.app.resources.SampleResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class SampleApplication extends Application<SampleConfiguration> {

    public static void main(final String[] args) throws Exception {
        new SampleApplication().run(args);
    }

    @Override
    public String getName() {
        return "sample-app";
    }

    @Override
    public void initialize(final Bootstrap<SampleConfiguration> bootstrap) {
        bootstrap.addBundle(swaggerBundle);
    }

    private final SwaggerBundle<SampleConfiguration> swaggerBundle = new SwaggerBundle<SampleConfiguration>() {
        @Override
        protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(SampleConfiguration configuration) {
            return configuration.getSwaggerBundleConfiguration();
        }
    };

    @Override
    public void run(final SampleConfiguration configuration,
                    final Environment environment) {
        environment.jersey().register(new SampleResource());
    }

}
