package com.truextend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.truextend.dao.StudentClassDAO;
import com.truextend.model.Class0;
import com.truextend.model.StudentClass;
import com.truextend.service.ClassService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/class")
public class ClassController {

    @Autowired
    ClassService classService;

    @Autowired
    StudentClassDAO studentClassDAO;


    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(position=1, value="Get a List of Classes")
    public Response list() throws JsonProcessingException {
        List<Class0> classes = classService.selectAll();
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(classes);
        return Response.status(200).header("content-type", MediaType.APPLICATION_JSON).entity(result).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(position=2, value="Select a Class")
    public Response selectUsers(@PathParam("id") Long id) throws JsonProcessingException {
        Class0 class0 = classService.selectById(id);
        if (class0 == null) {
            return Response.status(404).header("content-type", MediaType.APPLICATION_JSON).build();
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
    public Response insertUser( Map<String, Object> map ) throws JsonProcessingException {
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
    @ApiOperation(position=4, value="Update a Student")
    public Response updateUser( @PathParam("id") Long id, Map<String, Object> map ) throws JsonProcessingException {
        Class0 class0 = classService.selectById( id );
        if( class0 == null )
        {
            return Response.status(404).header("content-type", MediaType.APPLICATION_JSON).entity(String.format( "Student with Id[%d] not found", id)).build();
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
    @ApiOperation(position=5, value="Delete a Student (AUTO)")
    public Response deleteUser( @PathParam("id") Long id )
    {
        Class0 student = classService.selectById( id );
        if( student == null )
        {
            return Response.status(404).header("content-type", MediaType.APPLICATION_JSON).entity(String.format( "Student with Id[%d] not found", id)).build();
        }
        classService.delete(student);
        return Response.noContent().status( 204 ).header( "content-type", "application/json" ).build();
    }

    @GET
    @Path("/{classId}/class")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(position=1, value="Get a List of Classes")
    public Response listClasses(@PathParam("classId") Long classId) throws JsonProcessingException {
        Map parameters = new HashMap();
        parameters.put("class0.id", classId);
        List<StudentClass> studentsClass = studentClassDAO.selectAllBy(parameters);
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(studentsClass);
        return Response.status(200).header("content-type", MediaType.APPLICATION_JSON).entity(result).build();
    }

}
