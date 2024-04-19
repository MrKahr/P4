package com.proj4.symbolTable.symbols;

public abstract class SymbolTableEntry {

    private String type;

    public String getType(){
        return type;
    }
    
    public void setType(String type){
        this.type = type;
    }
}