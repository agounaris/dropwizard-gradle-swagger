package com.sample.app.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ApiAuthenticator implements Authenticator<BasicCredentials, User> {
    private List<UserConfiguration> users = new ArrayList<>();

    public ApiAuthenticator(List<UserConfiguration> users) {
        this.users = users;
    }

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        Optional<UserConfiguration> loggedUser = users.stream()
                .filter(u -> u.getName().equals(credentials.getUsername()) && u.getPassword().equals(credentials.getPassword())).findFirst();

        if (loggedUser.isPresent()) {
            User user = new User(loggedUser.get().getName(), loggedUser.get().getPassword(), loggedUser.get().getRoles());

            return Optional.of(user);
        }
        return Optional.empty();
    }
}
