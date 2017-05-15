package com.sample.app.resources;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Author
 */
@Path("v1/sample")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api("sample")
public class SampleResource {
    private static final Logger logger = LoggerFactory.getLogger(SampleResource.class);

    @GET
    @ApiOperation("Sample endpoint")
    public Response get() {
        logger.error("a log message");
        return Response.ok("{\"test\":\"test\"}").build();
    }
}
