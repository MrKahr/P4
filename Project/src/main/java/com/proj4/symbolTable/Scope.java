package com.proj4.symbolTable;

import java.util.HashMap;

//this class represents a scope in the programming language
public class Scope {
    private HashMap<String, Type> variableTable = new HashMap<>();
    private HashMap<String, Type> functionTable = new HashMap<>();

    public HashMap getVTable(){
        return variableTable;
    }
    public HashMap getFTable(){
        return functionTable;
    }
    public void setVtable(HashMap<String, Type> table){
        variableTable = table;
    }
    public void setFtable(HashMap<String, Type> table){
        functionTable = table;
    }
}