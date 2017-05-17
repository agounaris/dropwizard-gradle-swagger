package com.sample.app.resources;


import com.codahale.metrics.annotation.Timed;

import javax.annotation.security.RolesAllowed;

import com.google.inject.Inject;
import com.sample.app.SampleConfiguration;
import com.sample.app.api.PostDto;
import com.sample.app.auth.User;
import com.sample.app.core.Post;
import com.sample.app.dao.PostDao;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

/**
 * Author
 */
@Path("v1/sample")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api("sample")
public class SampleResource {
    private static final Logger logger = LoggerFactory.getLogger(SampleResource.class);

    private final SampleConfiguration configuration;

    @Resource
    private PostDao postDao;

    @Inject
    public SampleResource(SampleConfiguration configuration, PostDao postDao) {
        this.configuration = configuration;
        this.postDao = postDao;
    }

    @GET
    @ApiOperation("Sample endpoint")
    public Response get() {
        logger.error("a log message");
        return Response.ok("{\"test\":\"test\"}").build();
    }

    @GET
    @Path("/message")
    @ApiOperation("Sample endpoint")
    public Response getFromConfig() {
        logger.error("AN ERROR MESSAGE");
        return Response.ok(this.configuration.getMessage()).build();
    }

    @POST
    @Path("/post")
    @RolesAllowed({"write"})
    @ApiOperation("Creates a post")
    @Timed
    @UnitOfWork
    public Response createPost(@HeaderParam("X-Transaction-Id") String transactionId,
                               @ApiParam(value = "The post to be created", required = true) @NotNull @Valid PostDto postDto,
                               @Auth User user) {
        Post post = new Post();
        post.setContent(postDto.getContent());
        post.setType(postDto.getType());
        post.setCreated(new Date());
        post.setModified(new Date());
        post = postDao.create(post);

        return Response.ok(post).build();
    }

    @GET
    @Path("/post")
    @RolesAllowed({"read"})
    @ApiOperation("Gets all posts")
    @UnitOfWork
    public Response getAllPosts(@Auth User user) {
        return Response.ok(postDao.findAll()).build();
    }

    @GET
    @Path("/post/{id}")
    @RolesAllowed({"read"})
    @ApiOperation("Gets a single post")
    @UnitOfWork
    public Response getAllPosts(@ApiParam(value = "The asn to be created", required = true) @NotNull @Valid long id,
                                @Auth User user) {
        return Response.ok(postDao.findById(id)).build();
    }
}
