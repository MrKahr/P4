package com.proj4.symbolTable.symbols;

public class IntSymbol extends PrimitiveSymbol<Integer>{
 
    //Constructor
    public IntSymbol(Integer value){
        this.setType("Integer");
        this.setValue(value);
    }

    public IntSymbol(IntSymbol other){
        this.setType("Integer");
        this.setValue(other.getValue());
    }
}
