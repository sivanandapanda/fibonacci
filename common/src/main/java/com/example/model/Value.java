package com.example.model;

public class Value {
    private String index;
    private String value;

    public Value(String index, String value) {
        this.index = index;
        this.value = value;
    }

    public Value(String index) {
        this.index = index;
    }

    public Value() {
    }

    public String getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }
}
