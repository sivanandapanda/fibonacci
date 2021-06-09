package com.example.server;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.example.model.Value;

import io.quarkus.redis.client.reactive.ReactiveRedisClient;
import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.redis.client.Response;
import io.vertx.mutiny.sqlclient.Tuple;

@Path("/")
public class FibResource {

    @Inject
    PgPool client;

    @Inject
    ReactiveRedisClient redisClient;

    @PostConstruct
    public void init() {
        client.query("CREATE TABLE IF NOT EXISTS values (number INT)").execute()
        .subscribe()
        .with(item -> System.out.println(item + " table created successfully in postgres"), fail -> fail.printStackTrace());
    }

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
        redisClient.hgetall("values").onItem()
        .transform(response -> {
            Response response2 = response.get("values");
            System.out.print(response + " " + response2);
            return "1";
        }).subscribe();
        
        return null;
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
