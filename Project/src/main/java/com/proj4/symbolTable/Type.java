package com.proj4.symbolTable;

public abstract class Type {
    private String name;

    public Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}