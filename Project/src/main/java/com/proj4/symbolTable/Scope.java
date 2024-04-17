package com.proj4.symbolTable;

import java.util.HashMap;

//this class represents a scope in the programming language
public class Scope {
    //Field
    private HashMap<String, Type> variableTable = new HashMap<>();
    private HashMap<String, Type> functionTable = new HashMap<>();

    //Method
    public HashMap<String, Type> getVTable(){
        return variableTable;
    }

    public HashMap<String, Type> getFTable(){
        return functionTable;
    }

    public void setVtable(HashMap<String, Type> table){
        variableTable = table;
    }

    public void setFtable(HashMap<String, Type> table){
        functionTable = table;
    }
    
    //Copy all mappings from the specified scope to this scope, overwriting duplicates with mappings from the other scope
    private void putAll(Scope other){
        variableTable.putAll(other.getVTable());
        functionTable.putAll(other.getFTable());
    }

    @Override
    public Scope clone(){
        Scope clonedScope = new Scope();
        clonedScope.putAll(this);
        return clonedScope;
    }
}