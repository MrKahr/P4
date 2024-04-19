package com.proj4.symbolTable.symbols;

public class StringSymbol extends PrimitiveSymbol<String>{
    
    public StringSymbol(String value){
        this.setType("String");
        this.setValue(value);
    }
}
