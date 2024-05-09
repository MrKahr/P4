package com.proj4.symbolTable.symbols;

public class IntegerSymbol extends PrimitiveSymbol<Integer>{

    //Constructor
    public IntegerSymbol(Integer value){
        this.setType("Integer");
        this.setValue(value);
    }

    public IntegerSymbol(IntegerSymbol other){
        this.setType("Integer");
        this.setValue(other.getValue());
    }
}