package com.foo.prowler.dao;

import com.foo.prowler.api.Alert;
import com.foo.prowler.api.SearchRequest;
import org.hibernate.SessionFactory;

import java.util.List;

@SuppressWarnings("unchecked")
public class AlertDAO extends GenericDAO<Alert> {
    public AlertDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Alert> findAll() {
        var query = namedQuery("com.foo.prowler.api.Alert.findAll");
        return list(query);
    }

    public List<Alert> findByHostnameApplicationTs(SearchRequest request) {
        var query = namedQuery("com.foo.prowler.api.Alert.findByHostnameApplicationTs");
        query.setParameter("hostname", request.getHostname());
        query.setParameter("application", request.getApplication());
        query.setParameter("start", request.getStart());
        query.setParameter("end", request.getEnd());
        return list(query);
    }
}
