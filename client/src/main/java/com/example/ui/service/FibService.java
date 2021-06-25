package com.example.ui.service;

import com.example.ui.model.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.List;

import static java.util.Collections.emptyList;

@Service
public class FibService implements Serializable {

    @Autowired
    RestTemplate restTemplate;

    @org.springframework.beans.factory.annotation.Value("${fibonacci.server.url}")
    private String fibonacciServerUrl;

    public List<Value> getCurrent() {
        var currentValues = restTemplate.getForObject(fibonacciServerUrl+"/api/values/current", Value[].class);
        return currentValues != null ? List.of(currentValues) : emptyList();
    }

    public List<String> getAll() {
        var allValues = restTemplate.getForObject(fibonacciServerUrl + "/api/values/all", String[].class);
        return allValues != null ? List.of(allValues) : emptyList();
    }

    public String calculateFib(String index) {
        restTemplate.postForLocation(fibonacciServerUrl + "/api/values", new Value(index));
        return "Calculate " + index;
    }
}
