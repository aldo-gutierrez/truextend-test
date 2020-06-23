package com.truextend.dao;

import com.truextend.model.Student;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public class StudentDAO extends BaseHibernateDAO<Student, Long> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Override
    public Class<Student> getClazz() {
        return Student.class;
    }
}
