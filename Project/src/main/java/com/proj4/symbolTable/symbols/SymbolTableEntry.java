package com.proj4.symbolTable.symbols;

public abstract class SymbolTableEntry{
    //Field
    private String type;

    //Method
    public String getType(){
        return type;
    }
    
    public void setType(String type){
        this.type = type;
    }
}