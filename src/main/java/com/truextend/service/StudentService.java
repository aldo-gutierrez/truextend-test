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
public class StudentService {
    StudentDAO studentDAO;

    ClassDAO classDAO;

    StudentClassDAO studentClassDAO;

    @Autowired
    public void setStudentDAO(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @Autowired
    public void setClassDAO(ClassDAO classDAO) {
        this.classDAO = classDAO;
    }

    @Autowired
    public void setStudentClassDAO(StudentClassDAO studentClassDAO) {
        this.studentClassDAO = studentClassDAO;
    }

    public Student selectById(Long id) {
        return studentDAO.selectById(id);
    }

    public List<Student> selectAll() {
        return studentDAO.selectAll();
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
        Student otherStudent = studentDAO.selectBy("studentId", student.getStudentId());
        if (otherStudent != null) {
            throw new BusinessException("studentId is taken");
        }


        return studentDAO.insert(student);
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
        Student oldStudent = studentDAO.selectById(student.getId());
        if (oldStudent == null) {
            throw new BusinessException("student is on an Illegal state");
        }
        if (!oldStudent.getStudentId().equals(student.getStudentId())) {
            Student otherStudent = studentDAO.selectBy("studentId", student.getStudentId());
            if (otherStudent != null) {
                throw new BusinessException("studentId is taken");
            }
        }
        studentDAO.update(student);
    }

    public void delete(Student student) {
        Map parameters = new HashMap();
        parameters.put("student.id", student.getId());
        List<StudentClass> studentClasses = studentClassDAO.selectAllBy(parameters);
        for (StudentClass studentClass : studentClasses) {
            studentClassDAO.delete(studentClass);
        }
        studentDAO.delete(student);
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
