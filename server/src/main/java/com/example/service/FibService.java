package com.example.service;

import com.example.model.Value;
import io.quarkus.redis.client.reactive.ReactiveRedisClient;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class FibService {
    
    @Inject
    PgPool client;

    @Inject
    ReactiveRedisClient redisClient;

    @PostConstruct
    public void init() {
        client.query("CREATE TABLE IF NOT EXISTS values (number VARCHAR)").execute().await().indefinitely();
    }

    public static Integer from(Row row) {
        return row.getInteger("number");
    }

    public Uni<Response> submitFibIndexCalcRequest(Value value) {
        var redisHSet = redisClient.hset(List.of("values", String.valueOf(value.getIndex()), "Nothing yet!"));
        var redisPublish = redisClient.publish("insert", String.valueOf(value.getIndex()));
        var pgInsert = client.preparedQuery("INSERT INTO values(number) VALUES($1)").execute(Tuple.of(value.getIndex()));

        return Uni.combine().all()
                .unis(redisHSet, redisPublish, pgInsert)
                .combinedWith(listOfResponses -> Response.accepted().build());
    }

    public Multi<Value> getAllCurrentFromCache() {
        return redisClient.hgetall("values").onItem()
                .transformToMulti(response -> {
                    List<Value> values = new ArrayList<>();

                    for (String key : response.getKeys()) {
                        values.add(new Value(key, response.get(key).toString()));
                    }

                    return Multi.createFrom().iterable(values);
                });
    }

    public Multi<String> getAllValuesFromDb() {
        return client.query("SELECT DISTINCT number from values").execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(row -> row.getString("number"));
    }
}
