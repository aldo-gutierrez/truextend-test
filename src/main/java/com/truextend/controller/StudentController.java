package com.truextend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.truextend.dao.StudentDAO;
import com.truextend.model.Student;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/student")
@Transactional
public class StudentController {

    @Autowired
    StudentDAO studentDAO;


    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(position=1, value="Get a List of Students")
    public Response list() throws JsonProcessingException {
        List<Student> students = studentDAO.selectAll();
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(students);
        return Response.status(200).header("content-type", MediaType.APPLICATION_JSON).entity(result).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(position=2, value="Select a Student")
    public Response selectUsers(@PathParam("id") Long id) throws JsonProcessingException {
        Student student = studentDAO.selectById(id);
        if (student == null) {
            return Response.status(404).header("content-type", MediaType.APPLICATION_JSON).build();
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            String result = objectMapper.writeValueAsString(student);
            return Response.status(200).header("content-type", MediaType.APPLICATION_JSON).entity(result).build();
        }
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(position=3, value="Insert a Student")
    public Response insertUser( Map<String, Object> map ) throws JsonProcessingException {
        Student student = new Student();
        student.setFirstName((String) map.get("firstName"));
        student.setLastName((String) map.get("lastName"));
        student.setStudentId((String) map.get("studentId"));
        studentDAO.insert(student);
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(student);
        return Response.status(201).header("content-type", MediaType.APPLICATION_JSON).entity(result).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(position=4, value="Update a Student")
    public Response updateUser( @PathParam("id") Long id, Map<String, Object> map ) throws JsonProcessingException {
        Student student = studentDAO.selectById( id );
        if( student == null )
        {
            return Response.status(404).header("content-type", MediaType.APPLICATION_JSON).entity(String.format( "Student with Id[%d] not found", id)).build();
        }
        if (map.containsKey("firstName")) {
            student.setFirstName((String) map.get("firstName"));
        }
        if (map.containsKey("lastName")) {
            student.setFirstName((String) map.get("lastName"));
        }
        if (map.containsKey("studentId")) {
            student.setFirstName((String) map.get("studentId"));
        }
        studentDAO.update(student);

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(student);
        return Response.status(200).header("content-type", MediaType.APPLICATION_JSON).entity(result).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(position=5, value="Delete a Student (AUTO)")
    public Response deleteUser( @PathParam("id") Long id )
    {
        Student student = studentDAO.selectById( id );
        if( student == null )
        {
            return Response.status(404).header("content-type", MediaType.APPLICATION_JSON).entity(String.format( "Student with Id[%d] not found", id)).build();
        }
        studentDAO.selectById(id);
        return Response.noContent().status( 204 ).header( "content-type", "application/json" ).build();
    }

}
