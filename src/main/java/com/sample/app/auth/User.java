package com.sample.app.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import java.security.Principal;
import java.util.List;
import java.util.Random;

public class User implements Principal {
    Random random = new Random();

    @NotEmpty
    @JsonProperty
    private String name;

    @NotEmpty
    @JsonProperty
    private String password;

    @NotEmpty
    @JsonProperty
    private List<String> roles;

    /**
     * Constructor.
     *
     * @param name
     *            the name of the user
     * @param password
     *            the user's password
     */
    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.roles = null;
    }

    /**
     * Constructor.
     *
     * @param name
     *            the name of the user
     * @param password
     *            the user's password
     * @param roles
     *            the roles of the user
     */
    public User(String name, String password, List<String> roles) {
        this.name = name;
        this.password = password;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return  random.nextInt(100);
    }

    public List<String> getRoles() {
        return roles;
    }
}
