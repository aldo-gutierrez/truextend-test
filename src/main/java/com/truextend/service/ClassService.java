package com.truextend.service;

import com.truextend.dao.ClassDAO;
import com.truextend.dao.StudentClassDAO;
import com.truextend.dao.StudentDAO;
import com.truextend.model.Class0;
import com.truextend.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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

}
