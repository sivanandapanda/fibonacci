package com.example.worker;

import io.quarkus.redis.client.reactive.ReactiveRedisClient;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class FibCalculator {

    @Inject
    ReactiveRedisClient redisClient;

    @PostConstruct
    void init() {
        redisClient.subscribe(List.of("insert"))
                .subscribe()
                .with(item -> {
                    System.out.println(item);
                }, fail -> fail.printStackTrace());
    }

    private int fib(int index) {
        if(index < 2) return 1;

        return fib(index-1) + fib(index-2);
    }

    @PreDestroy
    void destroy() {
        redisClient.unsubscribe(List.of("insert"));
    }

}
