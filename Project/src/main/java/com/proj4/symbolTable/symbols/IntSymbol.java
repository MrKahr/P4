package com.proj4.symbolTable.symbols;

public class IntSymbol extends PrimitiveSymbol<Integer>{
 
    public IntSymbol(Integer value){
        this.setType("Integer");
        this.setValue(value);
    }
}
