package com.proj4.symbolTable.symbols;

public class NullSymbol extends PrimitiveSymbol<String>{

    public NullSymbol(String value){
        this.setType("Null");
        this.setValue(value);
    }

    public NullSymbol(NullSymbol other){
        this.setType("Null");
        this.setValue(other.getValue());
    }
}