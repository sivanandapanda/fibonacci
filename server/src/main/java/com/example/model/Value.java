package com.example.model;

import java.io.Serializable;

import io.vertx.mutiny.sqlclient.Row;

public class Value implements Serializable{
    
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
        return new Value(row.getInteger("number"));
    }
}
