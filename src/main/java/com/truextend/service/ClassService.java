package com.truextend.service;

import com.truextend.dao.*;
import com.truextend.exception.BusinessException;
import com.truextend.model.Class0;
import com.truextend.model.Student;
import com.truextend.model.StudentClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ClassService {
    Logger logger = LoggerFactory.getLogger(ClassService.class);

    IClassDAO classDAO;

    IStudentClassDAO studentClassDAO;

    IStudentDAO studentDAO;

    @Autowired
    public void setClassDAO(IClassDAO classDAO) {
        this.classDAO = classDAO;
    }

    @Autowired
    public void setStudentClassDAO(IStudentClassDAO studentClassDAO) {
        this.studentClassDAO = studentClassDAO;
    }

    @Autowired
    public void setStudentDAO(IStudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    public Class0 selectById(Long id) {
        return classDAO.findById(id).orElse(null);
    }

    public List<Class0> selectAll() {
        return classDAO.findAll();
    }

    public Long insert(Class0 class0) {
        if (StringUtils.isEmpty(class0.getCode())) {
            throw new BusinessException("code is required");
        }
        if (StringUtils.isEmpty(class0.getTitle())) {
            throw new BusinessException("title is required");
        }
        Class0 otherClass = classDAO.findByCode(class0.getCode());
        if (otherClass != null) {
            throw new BusinessException("code is taken");
        }
        Long id = classDAO.save(class0).getId();
        logger.debug("Inserted class {}.", class0.getCode());
        return id;
    }

    public void update(Class0 class0) {
        if (StringUtils.isEmpty(class0.getCode())) {
            throw new BusinessException("code is required");
        }
        if (StringUtils.isEmpty(class0.getTitle())) {
            throw new BusinessException("title is required");
        }
        Optional<Class0> oldClass = classDAO.findById(class0.getId());
        if (!oldClass.isPresent()) {
            throw new BusinessException("class is on an Illegal state");
        }
        if (!oldClass.get().getCode().equals(class0.getCode())) {
            Class0 otherClass = classDAO.findByCode(class0.getCode());
            if (otherClass != null) {
                throw new BusinessException("studentId is taken");
            }
        }
        classDAO.save(class0);
        logger.debug("Updated class {}.", class0.getCode());
    }

    public void delete(Class0 class0) {
        List<StudentClass> studentClasses = studentClassDAO.findAllByClass0Id(class0.getId());
        for (StudentClass studentClass : studentClasses) {
            studentClassDAO.delete(studentClass);
        }
        classDAO.delete(class0);
        logger.debug("Deleted class {}.", class0.getCode());
    }

    public List<Class0> selectAllByStudent(Student student) {
        List<StudentClass> studentsClass = studentClassDAO.findAllByStudentId(student.getId());
        return studentsClass.stream().map(StudentClass::getClass0).collect(Collectors.toList());
    }
}
