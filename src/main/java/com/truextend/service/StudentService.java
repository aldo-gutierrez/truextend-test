package com.truextend.service;

import com.truextend.dao.*;
import com.truextend.exception.BusinessException;
import com.truextend.model.Class0;
import com.truextend.model.Student;
import com.truextend.model.StudentClass;
import org.hibernate.criterion.Criterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import org.hibernate.criterion.Order;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class StudentService {
    Logger logger = LoggerFactory.getLogger(StudentService.class);

    StudentDAO oldStudentDAO;

    IStudentClassDAO studentClassDAO;

    IStudentDAO studentDAO;

    @Autowired
    public void setStudentDAO(IStudentDAO iStudentDAO) {
        this.studentDAO = iStudentDAO;
    }

    @Autowired
    public void setOldStudentDAO(StudentDAO oldStudentDAO) {
        this.oldStudentDAO = oldStudentDAO;
    }

    @Autowired
    public void setStudentClassDAO(IStudentClassDAO studentClassDAO) {
        this.studentClassDAO = studentClassDAO;
    }

    public Student selectById(Long id) {
        return studentDAO.findById(id).orElse(null);
    }

    public List<Student> selectAllBy(List<Criterion> criterionList, Pagination pagination, List<Order> orders) {
        return oldStudentDAO.selectAllBy(criterionList, pagination, orders);
    }

    public Long countAllBy(List<Criterion> criterionList) {
        return oldStudentDAO.countAllBy(criterionList);
    }

    public Long insert(Student student) {
        if (StringUtils.isEmpty(student.getStudentId())) {
            throw new BusinessException("studentId is required");
        }
        if (StringUtils.isEmpty(student.getFirstName())) {
            throw new BusinessException("firstName is required");
        }
        if (StringUtils.isEmpty(student.getLastName())) {
            throw new BusinessException("lastName is required");
        }
        Student otherStudent = studentDAO.findByStudentId(student.getStudentId());
        if (otherStudent != null) {
            throw new BusinessException("studentId is taken");
        }
        student = studentDAO.save(student);
        Long id = student.getId();
        logger.debug("Inserted student {}.", student.getStudentId());
        return id;
    }

    public void update(Student student) {
        if (StringUtils.isEmpty(student.getStudentId())) {
            throw new BusinessException("studentId is required");
        }
        if (StringUtils.isEmpty(student.getFirstName())) {
            throw new BusinessException("firstName is required");
        }
        if (StringUtils.isEmpty(student.getLastName())) {
            throw new BusinessException("lastName is required");
        }
        Optional<Student> oldStudent = studentDAO.findById(student.getId());
        if (!oldStudent.isPresent()) {
            throw new BusinessException("student is on an Illegal state");
        }
        if (!oldStudent.get().getStudentId().equals(student.getStudentId())) {
            Student otherStudent = studentDAO.findByStudentId(student.getStudentId());
            if (otherStudent != null) {
                throw new BusinessException("studentId is taken");
            }
        }
        studentDAO.save(student);
        logger.debug("Updated student {}.", student.getStudentId());
    }

    public void delete(Student student) {
        List<StudentClass> studentClasses = studentClassDAO.findAllByStudentId(student.getId());
        for (StudentClass studentClass : studentClasses) {
            studentClassDAO.delete(studentClass);
        }
        studentDAO.delete(student);
        logger.debug("Deleted student {}.", student.getStudentId());
    }

    public List<Student> selectAllByClass(Class0 class0) {
        List<StudentClass> studentsClass = studentClassDAO.findAllByClass0Id(class0.getId());
        return studentsClass.stream().map(StudentClass::getStudent).collect(Collectors.toList());
    }

    public StudentClass assignClassToStudent(Class0 class0, Student student) {
        List<StudentClass> studentClasses = studentClassDAO.findAllByClass0IdAndStudentId(class0.getId(), student.getId());
        if (studentClasses.size() > 0) {
            return studentClasses.get(0);
        } else {
            StudentClass studentClass = new StudentClass();
            studentClass.setClass0(class0);
            studentClass.setStudent(student);
            studentClassDAO.save(studentClass);
            return studentClass;
        }
    }

    public void unAssignClassToStudent(Class0 class0, Student student) {
        List<StudentClass> studentClasses = studentClassDAO.findAllByClass0IdAndStudentId(class0.getId(), student.getId());
        if (studentClasses.size() > 0) {
            for (StudentClass studentClass : studentClasses) {
                studentClassDAO.delete(studentClass);
            }
        }
    }


}
