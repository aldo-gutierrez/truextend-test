package com.truextend.dao;

import com.truextend.model.StudentClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IStudentClassDAO extends JpaRepository<StudentClass, Long> {
    List<StudentClass> findAllByStudentId(Long studentId);
    List<StudentClass> findAllByClass0Id(Long class0Id);
    List<StudentClass> findAllByClass0IdAndStudentId(Long class0Id, Long studentId);
}
