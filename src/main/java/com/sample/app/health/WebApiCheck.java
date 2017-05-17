package com.sample.app.health;

import com.codahale.metrics.health.HealthCheck;

public class WebApiCheck extends HealthCheck {
    private final String version;

    public WebApiCheck(String version) {
        this.version = version;
    }

    @Override
    protected Result check() throws Exception {
        return Result.healthy("OK with version: " + this.version);
    }
}
