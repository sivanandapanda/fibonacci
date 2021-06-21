package com.example.worker;

public class FibCalculator {

    public static void main(String[] args) {
        System.out.println("I am running!");
    }

    /*@Inject
    ReactiveRedisClient redisClient;

    @Inject
    Logger log;

    public void init(@Observes @Priority(2) StartupEvent event) {
        *//*redisClient.subscribe(List.of("insert")).onItem()
        .transform(response -> {
            var keys = response.getKeys();

        })
        ;*//*

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
    }*/

}
