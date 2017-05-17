package com.sample.app;

import com.sample.app.auth.UserConfiguration;
import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

import java.util.List;

public class SampleConfiguration extends Configuration {

    @NotEmpty
    @JsonProperty
    private String appId;

    @NotEmpty
    @JsonProperty
    private String appName;

    @NotEmpty
    @JsonProperty
    private String appVersion;

    @NotEmpty
    private String message;

    public String getMessage() {
        return message;
    }

    @Valid
    @JsonProperty("swagger")
    private SwaggerBundleConfiguration swaggerBundleConfiguration;

    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database;

    @NotEmpty
    @JsonProperty("users")
    private List<UserConfiguration> users;

    public SwaggerBundleConfiguration getSwaggerBundleConfiguration() {
        return swaggerBundleConfiguration;
    }

    public DataSourceFactory getDatabase() {
        return database;
    }

    public List<UserConfiguration> getUsers() {
        return users;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppVersion() {
        return appVersion;
    }
}
