package com.truextend.dao;

import com.truextend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStudentDAO extends JpaRepository<Student, Long> {
    Student findByStudentId(String studentId);
}
