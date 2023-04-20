package com.foo.prowler.resources;

import com.codahale.metrics.annotation.Timed;
import com.foo.prowler.api.ScanRequest;
import com.foo.prowler.core.ScanExecutor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;

@Path("/scan")
@Produces(MediaType.APPLICATION_JSON)
public class ScanResource {
    private final ScanExecutor scanExecutor;
    public ScanResource(ScanExecutor scanExecutor) {
        this.scanExecutor = scanExecutor;
    }

    @GET
    @Timed
    public Response getWelcomeMessage() throws URISyntaxException {
        return Response.ok("Welcome to Prowler Scan API").build();
    }

    @POST
    @Timed
    public Response startScan(ScanRequest request) throws URISyntaxException {
        switch (request.getCommand()) {
            case start -> scanExecutor.start();
            case stop -> scanExecutor.stop();
        }
        return Response.ok("Success").build();
    }
}