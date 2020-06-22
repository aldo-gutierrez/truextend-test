package com.truextend.dao;

import com.truextend.model.StudentClass;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class StudentClassDAO extends BaseHibernateDAO<StudentClass, Long>{
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Override
    public Class<StudentClass> getClazz() {
        return StudentClass.class;
    }
}