package com.example.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.sound.midi.Receiver;
import java.util.HashMap;
import java.util.Map;

public class FibCalculator {

    @Autowired
    StringRedisTemplate redisTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    public void receiveMessage(String message) {
        LOGGER.info("Received <" + message + ">");
        int calculatedFib = fib(Integer.parseInt(message), new HashMap<>());
        redisTemplate.opsForHash().put("values", message, String.valueOf(calculatedFib));
        LOGGER.info("For " + message + " fibonacci sequence is " + calculatedFib);
    }

    private int fib(int index, Map<Integer, Integer> cache) {
        if (index < 2) return 1;

        if (cache.containsKey(index)) return cache.get(index);

        int result = fib(index - 1, cache) + fib(index - 2, cache);
        cache.put(index, result);
        return result;
    }
}
