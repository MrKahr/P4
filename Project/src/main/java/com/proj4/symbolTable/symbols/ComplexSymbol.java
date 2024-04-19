package com.proj4.symbolTable.symbols;

import java.util.ArrayList;

//templates, actions, and arrays are all complex types.
//templates have a type and contain a number of fields, which also have types.
//and actions return some type (which could be null) and require arguments, which have types.
//arrays have a type and contain some content which has the same type.
//the difference between arrays, templates, and actions is how we type check them and in which symbol tables we place them.
public abstract class ComplexSymbol extends SymbolTableEntry {
    
    private ArrayList<SymbolTableEntry> content;

    public ArrayList<SymbolTableEntry> getContent(){
        return content;
    }

    public void setContent(ArrayList<SymbolTableEntry> content){
        this.content = content;
    }
}
