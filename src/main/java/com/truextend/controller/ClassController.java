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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(value="/class")
@Transactional
@RestController
public class ClassController {

    ClassService classService;

    StudentService studentService;

    @Autowired
    public void setClassService(ClassService classService) {
        this.classService = classService;
    }

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping(value = "/class", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(position=1, value="Get a List of Classes")
    public ResponseEntity<String> listClasses() throws JsonProcessingException {
        List<Class0> classes = classService.selectAll();
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(classes);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/class/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(position=2, value="Select a Class")
    public ResponseEntity<String> selectClass(@PathVariable("id") Long id) throws JsonProcessingException {
        Class0 class0 = classService.selectById( id );
        if (class0 == null) {
            throw new NotFoundException(String.format( "Class with Id[%d] not found", id));
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            String result = objectMapper.writeValueAsString(class0);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/class", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(position=3, value="Insert a Class")
    public ResponseEntity<String> insertClass(@RequestBody Map<String, Object> map ) throws JsonProcessingException {
        Class0 class0 = new Class0();
        class0.setCode((String) map.get("code"));
        class0.setTitle((String) map.get("title"));
        class0.setDescription((String) map.get("description"));
        classService.insert(class0);
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(class0);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping(value = "/class/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(position=4, value="Update a Class")
    public ResponseEntity<String> updateClass( @PathVariable("id") Long id,@RequestBody Map<String, Object> map ) throws JsonProcessingException {
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
            class0.setDescription((String) map.get("description"));
        }
        classService.update(class0);

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(class0);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(value = "/class/{id}")
    @ApiOperation(position=5, value="Delete a Class")
    public ResponseEntity<String> deleteClass( @PathVariable("id") Long id )
    {
        Class0 class0 = classService.selectById( id );
        if( class0 == null )
        {
            throw new NotFoundException(String.format( "Class with Id[%d] not found", id));
        }
        classService.delete(class0);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/class/{classId}/student", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(position=6, value="Get a List of Students for a Class")
    public ResponseEntity<String> listClasses(@PathVariable("classId") Long classId) throws JsonProcessingException {
        Class0 class0 = classService.selectById( classId );
        if( class0 == null )
        {
            throw new NotFoundException(String.format( "Class with Id[%d] not found", classId));
        }
        List<Student> students = studentService.selectAllByClass(class0);
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(students);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
