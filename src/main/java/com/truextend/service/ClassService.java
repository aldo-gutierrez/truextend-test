package com.truextend.service;

import com.truextend.dao.ClassDAO;
import com.truextend.dao.StudentClassDAO;
import com.truextend.dao.StudentDAO;
import com.truextend.exception.BusinessException;
import com.truextend.model.Class0;
import com.truextend.model.Student;
import com.truextend.model.StudentClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ClassService {

    ClassDAO classDAO;

    StudentClassDAO studentClassDAO;

    StudentDAO studentDAO;

    @Autowired
    public void setClassDAO(ClassDAO classDAO) {
        this.classDAO = classDAO;
    }

    @Autowired
    public void setStudentClassDAO(StudentClassDAO studentClassDAO) {
        this.studentClassDAO = studentClassDAO;
    }

    @Autowired
    public void setStudentDAO(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    public Class0 selectById(Long id) {
        return classDAO.selectById(id);
    }

    public List<Class0> selectAll() {
        return classDAO.selectAll();
    }

    public Long insert(Class0 class0) {
        if (StringUtils.isEmpty(class0.getCode())) {
            throw new BusinessException("code is required");
        }
        if (StringUtils.isEmpty(class0.getTitle())) {
            throw new BusinessException("title is required");
        }
        Class0 otherClass = classDAO.selectBy("code", class0.getCode());
        if (otherClass != null) {
            throw new BusinessException("code is taken");
        }
        return classDAO.insert(class0);
    }

    public void update(Class0 class0) {
        if (StringUtils.isEmpty(class0.getCode())) {
            throw new BusinessException("code is required");
        }
        if (StringUtils.isEmpty(class0.getTitle())) {
            throw new BusinessException("title is required");
        }
        Class0 oldClass = classDAO.selectById(class0.getId());
        if (oldClass == null) {
            throw new BusinessException("class is on an Illegal state");
        }
        if (!oldClass.getCode().equals(class0.getCode())) {
            Class0 otherClass = classDAO.selectBy("code", class0.getCode());
            if (otherClass != null) {
                throw new BusinessException("studentId is taken");
            }
        }
        classDAO.update(class0);
    }

    public void delete(Class0 class0) {
        Map parameters = new HashMap();
        parameters.put("class.id", class0.getId());
        List<StudentClass> studentClasses = studentClassDAO.selectAllBy(parameters);
        for (StudentClass studentClass : studentClasses) {
            studentClassDAO.delete(studentClass);
        }
        classDAO.delete(class0);
    }

    public List<Class0> selectAllByStudent(Student student) {
        Map parameters = new HashMap();
        parameters.put("student.id", student.getId());
        List<StudentClass> studentsClass = studentClassDAO.selectAllBy(parameters);
        List<Class0> classes = studentsClass.stream().map(studentClass -> studentClass.getClass0()).collect(Collectors.toList());
        return classes;
    }
}
