package com.foo.prowler.resources;

import com.codahale.metrics.annotation.Timed;
import com.foo.prowler.api.Alert;
import com.foo.prowler.api.AlertList;
import com.foo.prowler.api.SearchRequest;
import com.foo.prowler.dao.AlertDAO;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Path("/alerts")
@Produces(MediaType.APPLICATION_JSON)
public class AlertResource {
    private final AlertDAO dao;
    public AlertResource(AlertDAO dao) {
        this.dao = dao;
    }

    @GET
    @UnitOfWork
    @Timed
    public Response getAlerts() {
        List<Alert> alerts = dao.findAll();
        AlertList response = new AlertList(alerts.size(), alerts);
        return Response.ok(response).build();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    @Timed
    public Response getAlertById(@PathParam("id") LongParam id) {
        Alert alert = dao.findById(id.get());
        if (alert != null) {
            return Response.ok(alert).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @POST
    @UnitOfWork
    @Timed
    public Response createAlert(Alert alert) throws URISyntaxException {
        long id = dao.saveOrUpdate(alert).getId();

        // Don't repeat the same notification within 5 minutes
        SearchRequest searchRequest = SearchRequest.builder()
                .hostname(alert.getHostname())
                .application(alert.getApplication())
                .start(alert.getTs().minusMinutes(5))
                .end(alert.getTs())
                .build();
        List<Alert> recentAlerts = dao.findByHostnameApplicationTs(searchRequest);
        if(recentAlerts.isEmpty()) {
            // TODO: Send notification to Email/Chat/... queues
        }

        return Response.created(new URI("/alerts/" + id))
                .build();
    }

    @POST
    @Path("/search")
    @UnitOfWork
    @Timed
    public Response searchAlerts(SearchRequest request) {
        List<Alert> alerts = dao.findByHostnameApplicationTs(request);
        AlertList response = new AlertList(alerts.size(), alerts);
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    @Timed
    public Response removeAlertById(@PathParam("id") LongParam id) {
        Alert alert = dao.findById(id.get());
        if (alert != null) {
            dao.delete(alert);
            return Response.ok("Success").build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
}