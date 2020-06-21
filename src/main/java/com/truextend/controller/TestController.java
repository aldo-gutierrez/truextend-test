package com.truextend.controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class TestController {

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public Response echoTest() {
        return Response.status(200).header("content-type", MediaType.TEXT_PLAIN).status( 400 ).entity("start screen").build();
    }
}
