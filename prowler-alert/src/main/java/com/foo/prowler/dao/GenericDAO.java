package com.foo.prowler.dao;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.Collection;

public abstract class GenericDAO<T> extends AbstractDAO<T> {

    protected GenericDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public T saveOrUpdate(T model) {
        return persist(model);
    }

    public void saveOrUpdate(Collection<T> models) {
        models.forEach(this::persist);
    }

    public void update(T model) {
        currentSession().merge(model);
    }

    public T findById(Long id) {
        return get(id);
    }

    public void delete(T object) {
        if (object != null) {
            currentSession().delete(object);
        }
    }

    public int delete(Collection<T> objects) {
        objects.forEach(this::delete);
        return objects.size();
    }

}
