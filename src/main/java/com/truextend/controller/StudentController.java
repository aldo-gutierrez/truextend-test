package com.truextend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.truextend.dao.Pagination;
import com.truextend.dao.PaginationHelper;
import com.truextend.exception.NotFoundException;
import com.truextend.model.Class0;
import com.truextend.model.Student;
import com.truextend.service.ClassService;
import com.truextend.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/student")
@Api(value="/student")
@Transactional
public class StudentController {

    StudentService studentService;

    ClassService classService;

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @Autowired
    public void setClassService(ClassService classService) {
        this.classService = classService;
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(position=1, value="Get a List of Students")
    public Response listStudents(@QueryParam("pageSize") Integer pageSize, @QueryParam("pageNumber") Integer pageNumber,
                                 @QueryParam("order") String order, @QueryParam("criteria") String criteria) throws JsonProcessingException {

        Pagination pagination = new Pagination(pageNumber, pageSize);
        List<Order> orders = PaginationHelper.parseOrders(order);
        List<Criterion> criterionList = PaginationHelper.parseCriteria(criteria);
        List<Student> students = studentService.selectAllBy(criterionList, pagination, orders);
        Long studentCount = studentService.countAllBy(criterionList);
        Map<String,Object> mapResponse = new HashMap<String,Object>();
        mapResponse.put( "total", studentCount );
        mapResponse.put( "results", students );
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(mapResponse);
        return Response.status(200).header("content-type", MediaType.APPLICATION_JSON).entity(result).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(position=2, value="Select a Student")
    public Response selectStudent(@PathParam("id") Long id) throws JsonProcessingException {
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
    public Response insertStudent( Map<String, Object> map ) throws JsonProcessingException {
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
    public Response updateStudent( @PathParam("id") Long id, Map<String, Object> map ) throws JsonProcessingException {
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
    @ApiOperation(position=5, value="Delete a Student")
    public Response deleteStudent( @PathParam("id") Long id )
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
    @ApiOperation(position=6, value="Get a List of Classes")
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
    @ApiOperation(position=7, value = "Assign a Class to a Student")
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
    @ApiOperation(position=8, value = "Unassign a Class to a Student")
    public Response unassignClassToStudent(@PathParam("studentId") Long studentId, @PathParam("classId") Long classId) {
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
