package com.sample.app;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;

import javax.validation.Valid;
import javax.validation.constraints.*;

import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class SampleConfiguration extends Configuration {

    @NotEmpty
    private String message;

    public String getMessage() {
        return message;
    }

    @Valid
    @JsonProperty("swagger")
    private SwaggerBundleConfiguration swaggerBundleConfiguration;

    public SwaggerBundleConfiguration getSwaggerBundleConfiguration() {
        return swaggerBundleConfiguration;
    }
}
