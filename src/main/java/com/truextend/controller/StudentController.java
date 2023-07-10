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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value="/student")
@RestController
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

    @GetMapping(value = "/student", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(position=1, value="Get a List of Students")
    public ResponseEntity<String> listStudents(@RequestParam(value = "pageSize", required = false) Integer pageSize, @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                               @RequestParam(value = "order", required = false) String order, @RequestParam(value = "criteria", required = false) String criteria) throws JsonProcessingException {

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
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/student/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(position=2, value="Select a Student")
    public ResponseEntity<String>  selectStudent(@PathVariable("id") Long id) throws JsonProcessingException {
        Student student = studentService.selectById(id);
        if (student == null) {
            throw new NotFoundException(String.format( "Student with Id[%d] not found", id));
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            String result = objectMapper.writeValueAsString(student);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/student", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(position=3, value="Insert a Student")
    public ResponseEntity<String>  insertStudent( @RequestBody Map<String, Object> map ) throws JsonProcessingException {
        Student student = new Student();
        student.setFirstName((String) map.get("firstName"));
        student.setLastName((String) map.get("lastName"));
        student.setStudentId((String) map.get("studentId"));
        studentService.insert(student);
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(student);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping(value = "/student/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(position=4, value="Update a Student")
    public ResponseEntity<String>  updateStudent( @PathVariable("id") Long id,@RequestBody Map<String, Object> map ) throws JsonProcessingException {
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
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(value = "/student/{id}")
    @ApiOperation(position=5, value="Delete a Student")
    public ResponseEntity<String>  deleteStudent( @PathVariable("id") Long id )
    {
        Student student = studentService.selectById( id );
        if( student == null )
        {
            throw new NotFoundException(String.format( "Student with Id[%d] not found", id));
        }
        studentService.delete(student);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/student/{studentId}/class", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(position=6, value="Get a List of Classes")
    public ResponseEntity<String>  listClasses(@PathVariable("studentId") Long studentId) throws JsonProcessingException {
        Student student = studentService.selectById( studentId );
        if( student == null )
        {
            throw new NotFoundException(String.format( "Student with Id[%d] not found", studentId));
        }
        List<Class0> classes = classService.selectAllByStudent(student);
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(classes);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(value = "/student/{studentId}/class/{classId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(position=7, value = "Assign a Class to a Student")
    public ResponseEntity<String>  assignClassToStudent(@PathVariable("studentId") Long studentId, @PathVariable("classId") Long classId) throws JsonProcessingException {
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
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/student/{studentId}/class/{classId}")
    @ApiOperation(position=8, value = "Unassign a Class to a Student")
    public ResponseEntity<String>  unassignClassToStudent(@PathVariable("studentId") Long studentId, @PathVariable("classId") Long classId) {
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
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
