package com.truextend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.truextend.exception.NotFoundException;
import com.truextend.model.Class0;
import com.truextend.model.Student;
import com.truextend.service.ClassService;
import com.truextend.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/class")
@Api(value="/class")
@Transactional
public class ClassController {

    @Autowired
    ClassService classService;

    @Autowired
    StudentService studentService;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(position=1, value="Get a List of Classes")
    public Response listClasses() throws JsonProcessingException {
        List<Class0> classes = classService.selectAll();
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(classes);
        return Response.status(200).header("content-type", MediaType.APPLICATION_JSON).entity(result).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(position=2, value="Select a Class")
    public Response selectClass(@PathParam("id") Long id) throws JsonProcessingException {
        Class0 class0 = classService.selectById( id );
        if (class0 == null) {
            throw new NotFoundException(String.format( "Class with Id[%d] not found", id));
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            String result = objectMapper.writeValueAsString(class0);
            return Response.status(200).header("content-type", MediaType.APPLICATION_JSON).entity(result).build();
        }
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(position=3, value="Insert a Class")
    public Response insertClass( Map<String, Object> map ) throws JsonProcessingException {
        Class0 class0 = new Class0();
        class0.setCode((String) map.get("code"));
        class0.setTitle((String) map.get("title"));
        class0.setDescription((String) map.get("description"));
        classService.insert(class0);
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(class0);
        return Response.status(201).header("content-type", MediaType.APPLICATION_JSON).entity(result).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(position=4, value="Update a Class")
    public Response updateClass( @PathParam("id") Long id, Map<String, Object> map ) throws JsonProcessingException {
        Class0 class0 = classService.selectById( id );
        if( class0 == null )
        {
            throw new NotFoundException(String.format( "Class with Id[%d] not found", id));
        }
        if (map.containsKey("code")) {
            class0.setCode((String) map.get("code"));
        }
        if (map.containsKey("title")) {
            class0.setTitle((String) map.get("title"));
        }
        if (map.containsKey("description")) {
            class0.setDescription((String) map.get("title"));
        }
        classService.update(class0);

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(class0);
        return Response.status(200).header("content-type", MediaType.APPLICATION_JSON).entity(result).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(position=5, value="Delete a Class")
    public Response deleteClass( @PathParam("id") Long id )
    {
        Class0 class0 = classService.selectById( id );
        if( class0 == null )
        {
            throw new NotFoundException(String.format( "Class with Id[%d] not found", id));
        }
        classService.delete(class0);
        return Response.noContent().status( 204 ).header( "content-type", "application/json" ).build();
    }

    @GET
    @Path("/{classId}/student")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(position=6, value="Get a List of Students for a Class")
    public Response listClasses(@PathParam("classId") Long classId) throws JsonProcessingException {
        Class0 class0 = classService.selectById( classId );
        if( class0 == null )
        {
            throw new NotFoundException(String.format( "Class with Id[%d] not found", classId));
        }
        List<Student> students = studentService.selectAllByClass(class0);
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(students);
        return Response.status(200).header("content-type", MediaType.APPLICATION_JSON).entity(result).build();
    }

}
