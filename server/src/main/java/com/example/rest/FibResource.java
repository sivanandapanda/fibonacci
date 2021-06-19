package com.example.rest;

import com.example.model.Value;
import com.example.service.FibService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/api")
public class FibResource {

    @Inject
    FibService fibService;

    @GET
    public String getDefault() {
        return "Hello";
    }

    @GET
    @Path("/values/all")
    public Multi<Value> getAllValues() {
        return fibService.getAllValuesFromDb();
    }

    @GET
    @Path("/values/current")
    public Multi<String> getAllCurrent() {
        return fibService.getAllCurrentFromCache();
    }

    @POST
    @Path("/values")
    public Uni<Response> submitFibIndexCalcRequest(Value value) {
        return fibService.submitFibIndexCalcRequest(value);
    }
}
