package com.truextend.controller;
import com.truextend.service.FooBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Path("/foo")
public class FooResource {

    @Autowired
    FooBean fooBean;

    @GET
    public String getFoo(@Context ServletContext context) {
        return context.getInitParameterNames().toString();
    }

    @GET
    @Path("/hello")
    public String hello() {
        return fooBean.hello();
    }
}