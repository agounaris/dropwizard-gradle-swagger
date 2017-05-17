package com.sample.app.auth;

import io.dropwizard.auth.Authorizer;

import java.util.List;

public class ApiAuthorizer implements Authorizer<User> {
    @Override
    public boolean authorize(User user, String role) {
        List<String> userRoles = user.getRoles();
        return userRoles != null && userRoles.contains(role);
    }
}
