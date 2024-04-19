package com.proj4.symbolTable.symbols;

public class PrimitiveSymbol<T> extends SymbolTableEntry{
    
    private T value;

    public T getValue(){
        return value;
    }
    
    public void setValue(T value){
        this.value = value;
    }
}
