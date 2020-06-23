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

@Path("/student")
@Api(value="/student")
@Transactional
public class StudentController {

    @Autowired
    StudentService studentService;

    @Autowired
    ClassService classService;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(position=1, value="Get a List of Students")
    public Response list() throws JsonProcessingException {
        List<Student> students = studentService.selectAll();
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(students);
        return Response.status(200).header("content-type", MediaType.APPLICATION_JSON).entity(result).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(position=2, value="Select a Student")
    public Response selectUsers(@PathParam("id") Long id) throws JsonProcessingException {
        Student student = studentService.selectById(id);
        if (student == null) {
            throw new NotFoundException(String.format( "Student with Id[%d] not found", id));
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
        studentService.insert(student);
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
        Student student = studentService.selectById( id );
        if( student == null )
        {
            throw new NotFoundException(String.format( "Student with Id[%d] not found", id));
        }
        if (map.containsKey("firstName")) {
            student.setFirstName((String) map.get("firstName"));
        }
        if (map.containsKey("lastName")) {
            student.setLastName((String) map.get("lastName"));
        }
        if (map.containsKey("studentId")) {
            student.setStudentId((String) map.get("studentId"));
        }
        studentService.update(student);

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
        Student student = studentService.selectById( id );
        if( student == null )
        {
            throw new NotFoundException(String.format( "Student with Id[%d] not found", id));
        }
        studentService.delete(student);
        return Response.noContent().status( 204 ).header( "content-type", "application/json" ).build();
    }

    @GET
    @Path("/{studentId}/class")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(position=1, value="Get a List of Classes")
    public Response listClasses(@PathParam("studentId") Long studentId) throws JsonProcessingException {
        Student student = studentService.selectById( studentId );
        if( student == null )
        {
            throw new NotFoundException(String.format( "Student with Id[%d] not found", studentId));
        }
        List<Class0> classes = classService.selectAllByStudent(student);
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(classes);
        return Response.status(200).header("content-type", MediaType.APPLICATION_JSON).entity(result).build();
    }

    @PUT
    @Path("/{studentId}/class/{classId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Assign Class to a Student")
    public Response assignClassToStudent(@PathParam("studentId") Long studentId, @PathParam("classId") Long classId) throws JsonProcessingException {
        Student student = studentService.selectById( studentId );
        if( student == null )
        {
            throw new NotFoundException(String.format( "Student with Id[%d] not found", studentId));
        }
        Class0 class0 = classService.selectById( classId );
        if( class0 == null )
        {
            throw new NotFoundException(String.format( "Class with Id[%d] not found", studentId));
        }

        studentService.assignClassToStudent(class0, student);
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(class0);
        return Response.status(201).header("content-type", MediaType.APPLICATION_JSON).entity(result).build();
    }

    @DELETE
    @Path("/{studentId}/class/{classId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Remove Role from a User")
    public Response unassignClassToStudent(@PathParam("studentId") Long studentId, @PathParam("classId") Long classId) throws JsonProcessingException {
        Student student = studentService.selectById( studentId );
        if( student == null )
        {
            throw new NotFoundException(String.format( "Student with Id[%d] not found", studentId));
        }
        Class0 class0 = classService.selectById( classId );
        if( class0 == null )
        {
            throw new NotFoundException(String.format( "Class with Id[%d] not found", studentId));
        }
        studentService.unAssignClassToStudent(class0, student);
        return Response.noContent().status( 204 ).header( "content-type", "application/json" ).build();
    }
}
