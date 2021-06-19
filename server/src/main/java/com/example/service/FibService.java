package com.example.service;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import com.example.model.Value;
import io.quarkus.redis.client.reactive.ReactiveRedisClient;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;

import java.util.List;

@ApplicationScoped
public class FibService {
    
    @Inject
    PgPool client;

    @Inject
    ReactiveRedisClient redisClient;

    @PostConstruct
    public void init() {
        client.query("CREATE TABLE IF NOT EXISTS values (number INT)").execute().await().indefinitely();
    }

    public static Value from(Row row) {
        return new Value(row.getInteger("number"));
    }

    public Uni<Response> submitFibIndexCalcRequest(Value value) {
        var redisHSet = redisClient.hset(List.of("values", String.valueOf(value.getIndex()), "Nothing yet!"));
        var redisPublish = redisClient.publish("insert", String.valueOf(value.getIndex()));
        var pgInsert = client.preparedQuery("INSERT INTO values(number) VALUES($1)").execute(Tuple.of(value.getIndex()));

        return Uni.combine().all()
                .unis(redisHSet, redisPublish, pgInsert)
                .combinedWith(listOfResponses -> Response.accepted().build());
    }

    public Multi<String> getAllCurrentFromCache() {
        return redisClient.hgetall("values").onItem()
                .transformToMulti(response -> {
                    return Multi.createFrom().iterable(List.of(response.toString()));
                    //List<String> collect = response.getKeys().stream().map(k -> response.get(k).toString()).collect(Collectors.toList());
                    //return Multi.createFrom().iterable(collect);
                });
    }

    public Multi<Value> getAllValuesFromDb() {
        return client.query("SELECT * from values").execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(FibService::from);
    }
}
