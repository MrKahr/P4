package com.proj4.symbolTable.symbols;

public class PrimitiveSymbol<T> extends SymbolTableEntry{
    //Field
    private T value;
    //Method
    public T getValue(){
        return value;
    }
    
    public void setValue(T value){
        this.value = value;
    }
}
