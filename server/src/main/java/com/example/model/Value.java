package com.example.model;

import io.vertx.mutiny.sqlclient.Row;

public class Value {
    private int index;

    public Value() {}

    public Value(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public static Value from(Row row) {
        return new Value(row.getInteger("index"));
    }
}
