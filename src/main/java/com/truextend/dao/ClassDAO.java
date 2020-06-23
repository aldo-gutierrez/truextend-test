package com.truextend.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.truextend.model.Class0;

@Repository
@Transactional
public class ClassDAO extends BaseHibernateDAO<Class0, Long> {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Override
    public Class<Class0> getClazz() {
        return Class0.class;
    }
}
