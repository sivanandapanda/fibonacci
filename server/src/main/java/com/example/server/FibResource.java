package com.example.server;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.example.model.Value;

import io.quarkus.redis.client.reactive.ReactiveRedisClient;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.redis.client.Response;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;

@Path("/")
public class FibResource {

    @Inject
    PgPool client;

    @Inject
    ReactiveRedisClient redisClient;

    @GET
    public String getDefault() {
        return "Hello";
    }

    @GET
    @Path("/values/all")
    public List<Value> getAllValues() {

        List<Value> result = new ArrayList<>();

        client.query("SELECT * from values").execute().onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set)).onItem()
                .transform(mapper -> Value.from(mapper)).collect().asList().subscribe()
                .with(item -> result.addAll(item), fail -> fail.printStackTrace());

        return result;
    }

    @GET
    @Path("/values/current")
    public Object getCurrent() {
        Uni<Response> hgetall = redisClient.hgetall("values");
        
        
        return null;
    }

}
