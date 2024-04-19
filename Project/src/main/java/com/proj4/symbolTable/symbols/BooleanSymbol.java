package com.proj4.symbolTable.symbols;

public class BooleanSymbol extends PrimitiveSymbol<Boolean>{

    public BooleanSymbol(Boolean value){
        this.setType("Boolean");
        this.setValue(value);
    }
}
