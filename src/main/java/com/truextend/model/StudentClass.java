package com.truextend.model;

import javax.persistence.*;

@Entity
public class StudentClass {
    Long id;
    Student student;
    Class0 class0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @ManyToOne
    public Class0 getClass0() {
        return class0;
    }

    public void setClass0(Class0 assignedClass) {
        this.class0 = assignedClass;
    }
}
