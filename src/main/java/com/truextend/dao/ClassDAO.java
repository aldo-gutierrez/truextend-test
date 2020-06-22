package com.truextend.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ClassDAO extends BaseHibernateDAO<Class, Long> {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Override
    public Class<Class> getClazz() {
        return Class.class;
    }
}
