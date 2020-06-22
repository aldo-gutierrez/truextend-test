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
public class StudentService {
    @Autowired
    StudentDAO dao;

    @Autowired
    ClassDAO classDAO;

    @Autowired
    StudentClassDAO studentClassDAO;

    public Student selectById(Long id) {
        return dao.selectById(id);
    }

    public List<Student> selectAll() {
        return dao.selectAll();
    }

    public Long insert(Student student) {
        return dao.insert(student);
    }

    public void update(Student student) {
        dao.update(student);
    }

    public void delete(Student student) {
        dao.delete(student);
    }

    public List<Student> selectAllByClass(Class0 class0) {
        Map parameters = new HashMap();
        parameters.put("class0.id", class0.getId());
        List<StudentClass> studentsClass = studentClassDAO.selectAllBy(parameters);
        List<Student> students = studentsClass.stream().map(studentClass -> studentClass.getStudent()).collect(Collectors.toList());
        return students;
    }

    public StudentClass assignClassToStudent(Class0 class0, Student student) {
        Map parameters = new HashMap();
        parameters.put("class0.id", class0.getId());
        parameters.put("student.id", student.getId());
        List<StudentClass> studentClasses = studentClassDAO.selectAllBy(parameters);
        if (studentClasses.size() > 0) {
            return studentClasses.get(0);
        } else {
            StudentClass studentClass = new StudentClass();
            studentClass.setClass0(class0);
            studentClass.setStudent(student);
            studentClassDAO.insert(studentClass);
            return studentClass;
        }
    }

    public void unAssignClassToStudent(Class0 class0, Student student) {
        Map parameters = new HashMap();
        parameters.put("class0.id", class0.getId());
        parameters.put("student.id", student.getId());
        List<StudentClass> studentClasses = studentClassDAO.selectAllBy(parameters);
        if (studentClasses.size() > 0) {
            for (StudentClass studentClass : studentClasses) {
                studentClassDAO.delete(studentClass);
            }
        } else {
        }
    }


}
