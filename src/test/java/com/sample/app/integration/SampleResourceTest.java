package com.sample.app.integration;

import com.sample.app.SampleApplication;
import com.sample.app.SampleConfiguration;
import com.sample.app.api.PostDto;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class SampleResourceTest {
    private static final String SAMPLE_RESOURCE_URL = "http://localhost:%d/api/v1/sample";
    private static final String POST_MODEL_URL = SAMPLE_RESOURCE_URL + "/post";

    @ClassRule
    public static final DropwizardAppRule<SampleConfiguration> RULE = new DropwizardAppRule<SampleConfiguration>(
            SampleApplication.class, ResourceHelpers.resourceFilePath("sample-test.yml"));

    @BeforeClass
    public static void createPost() {
        PostDto postDto = new PostDto();
        postDto.setContent("test");
        postDto.setType("test");

        Client client = new JerseyClientBuilder().build();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "admin");
        client.register(feature);

        Response responseCreate = client
                .target(String.format(POST_MODEL_URL, 8080))
                .request()
                .post(Entity.json(postDto),
                        Response.class);

        System.out.println(responseCreate.getStatus());
        assertThat(responseCreate).isNotNull();
        assertThat(responseCreate.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        assertThat(responseCreate.getLocation()).isNotNull();
    }

    @Test
    public void testGetPostAsAdmin() {
        Client client = new JerseyClientBuilder().build();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "admin");
        client.register(feature);
        Response responseGet = client
                .target(String.format(POST_MODEL_URL, 8080))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(responseGet.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }
}
