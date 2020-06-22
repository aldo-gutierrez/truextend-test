package com.truextend.service;

import com.truextend.dao.ClassDAO;
import com.truextend.dao.StudentClassDAO;
import com.truextend.dao.StudentDAO;
import com.truextend.model.Class0;
import com.truextend.model.Student;
import com.truextend.model.StudentClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ClassService {

    @Autowired
    ClassDAO dao;

    @Autowired
    StudentClassDAO studentClassDAO;

    @Autowired
    StudentDAO studentDAO;


    public Class0 selectById(Long id) {
        return dao.selectById(id);
    }

    public List<Class0> selectAll() {
        return dao.selectAll();
    }

    public Long insert(Class0 student) {
        return dao.insert(student);
    }

    public void update(Class0 student) {
        dao.update(student);
    }

    public void delete(Class0 student) {
        dao.delete(student);
    }

    public List<Class0> selectAllByStudent(Student student) {
        Map parameters = new HashMap();
        parameters.put("student.id", student.getId());
        List<StudentClass> studentsClass = studentClassDAO.selectAllBy(parameters);
        List<Class0> classes = studentsClass.stream().map(studentClass -> studentClass.getClass0()).collect(Collectors.toList());
        return classes;
    }
}
