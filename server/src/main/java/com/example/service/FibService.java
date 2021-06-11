package com.example.service;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.vertx.mutiny.pgclient.PgPool;

@ApplicationScoped
public class FibService {
    
    @Inject
    PgPool client;

    @PostConstruct
    public void init() {
        client.query("CREATE TABLE IF NOT EXISTS values (number INT)").execute().await().indefinitely();
    }

}
