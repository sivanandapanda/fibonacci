package com.example.server;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.example.model.Value;
import com.example.service.FibService;

import io.quarkus.redis.client.reactive.ReactiveRedisClient;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.redis.client.Response;
import io.vertx.mutiny.sqlclient.Tuple;

@Path("/")
public class FibResource {

    @Inject
    PgPool client;

    @Inject
    ReactiveRedisClient redisClient;

    @Inject
    FibService fibService;

    @GET
    public String getDefault() {
        return "Hello";
    }

    @GET
    @Path("/values/all")
    public Multi<Value> getAllValues() {
        return client.query("SELECT * from values").execute()
        .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
        .onItem().transform(mapper -> Value.from(mapper));
    }

    @GET
    @Path("/values/current")
    public Multi<String> getCurrent() {
        return redisClient.hgetall("values").onItem()
        .transformToMulti(response -> {
            List<String> collect = response.getKeys().stream().map(k -> response.get(k).toString()).collect(Collectors.toList());
            return Multi.createFrom().iterable(collect);
        });
    }

    @POST
    @Path("/values")
    public Object findFibIndex(Value value) {
        redisClient.hset(List.of("values", String.valueOf(value.getIndex()), "Nothing yet!"))
        .subscribe()
        .with(item -> System.out.println(item + " stored successfully in redis"), fail -> fail.printStackTrace());

        redisClient.publish("insert", String.valueOf(value.getIndex()))
        .subscribe()
        .with(item -> System.out.println(item + " published successfully in redis"), fail -> fail.printStackTrace());
        
        client.preparedQuery("INSERT INTO values(number) VALUES($1)").execute(Tuple.of(value.getIndex()))
        .subscribe()
        .with(item -> System.out.println(item + " inserted successfully in postgres"), fail -> fail.printStackTrace());
        
        return null;
    }
}
