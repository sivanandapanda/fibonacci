package com.example.worker;

import io.quarkus.redis.client.reactive.ReactiveRedisClient;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Multi;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FibCalculator {

    @Inject
    ReactiveRedisClient redisClient;

    public void init(@Observes @Priority(2) StartupEvent event) {
        /*redisClient.subscribe(List.of("insert")).onItem()
        .transform(response -> {
            var keys = response.getKeys();

        })
        ;*/

        Multi.createBy().repeating()
                .supplier(() -> this.redisClient.subscribe(List.of("insert"))
                        .onItem().transformToMulti(keys -> Multi.createFrom().iterable(keys))
                        .onItem().castTo(String.class))
                .indefinitely()
                .onItem().disjoint()
                .onOverflow().drop(res -> log.warn("Dropping msg: {}", res))
                .subscribe().with(res -> log.debug("Processed msg({})", res), err -> log.error("Caught exception: {}", err));

    }

    private int fib(int index) {
        return fib(index, new HashMap<>());
    }

    private int fib(int index, Map<Integer, Integer> cache) {
        if(index < 2) return 1;

        if(cache.containsKey(index)) return cache.get(index);

        int result = fib(index-1, cache) + fib(index-2, cache);
        cache.put(index, result);
        return result;
    }

    @PreDestroy
    void destroy() {
        redisClient.unsubscribe(List.of("insert")).await().atMost(Duration.ofSeconds(5));
    }

}
