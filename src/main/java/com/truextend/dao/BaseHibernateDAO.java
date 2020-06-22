package com.truextend.dao;

import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class BaseHibernateDAO<T, K extends Serializable> {

    public abstract SessionFactory getSessionFactory();

    public abstract Class<T> getClazz();

    public Session getSession() {
        return getSessionFactory().getCurrentSession();
    }

    public T selectById(K id) {
        return (T) getSession().get(getClazz(), id);
    }

    public T selectBy(String property, Object value) {
        Criteria c1 = getSession().createCriteria(getClazz());
        c1.add(Restrictions.eq(property, value));
        return (T) c1.uniqueResult();
    }

    public List<T> selectAll() {
        Query query = getSession().createQuery("from " + getClazz().getName());
        return (List<T>) query.list();
    }

    public List<T> selectAllBy(Map<String, Object> map) {
        Criteria c1 = getSession().createCriteria(getClazz());
        for (String key : map.keySet()) {
            c1.add(Restrictions.eq(key, map.get(key)));
        }
        return (List<T>) c1.list();
    }

    public K insert(T object) {
        Session session = getSession();
        session.save(object);
        K result = (K) session.getIdentifier(object);
        return result;
    }

    public void update(T object) {
        Session session = getSession();
        session.update(object);
    }

    public void delete(T object) {
        Session session = getSession();
        session.delete(object);
    }
}
