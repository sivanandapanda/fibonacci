package com.example.ui;

import java.io.Serializable;
import java.util.List;

import com.example.ui.model.Value;
import org.springframework.stereotype.Service;

@Service
public class FibService implements Serializable {

    public List<String> getCurrent() {
        return List.of("1", "2", "3", "4");
    }

    public List<Value> getAll() {
        return List.of(new Value(1), new Value(2), new Value(3));
    }

    public String calculateFib(String index) {
        return "Will calculate " + index;
    }
}
