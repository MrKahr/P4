package com.proj4.symbolTable;

import java.util.HashMap;
import java.util.HashSet;

import com.proj4.exceptions.StateAlreadyDefinedExpection;
import com.proj4.exceptions.VariableAlreadyDefinedException;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

//this class represents a scope in the programming language
public class Scope {
    //Field
    private HashMap<String, SymbolTableEntry> variableTable = new HashMap<>();
    private HashMap<String, SymbolTableEntry> functionTable = new HashMap<>();
    private HashSet<String> declaredTable = new HashSet<>();   //this table keeps track of whether or not a variable or function has been declared in this scope
    private HashSet<String> stateTable = new HashSet<>(); //this table keeps track of which states have been declared in the current scopes
    
    //Method
    public HashMap<String, SymbolTableEntry> getVTable(){
        return variableTable;
    }

    public HashMap<String, SymbolTableEntry> getFTable(){
        return functionTable;
    }

    public HashSet<String> getDTable(){
        return declaredTable;
    }

    public HashSet<String> getStateTable(){
        return stateTable;
    }

    public void setVTable(HashMap<String, SymbolTableEntry> table){
        variableTable = table;
    }

    public void setFTable(HashMap<String, SymbolTableEntry> table){
        functionTable = table;
    }
    
    public void setDTable(HashSet<String> table){
        declaredTable = table;
    }

    public void setStateTabel(HashSet<String> table){
        stateTable = table;
    }


    //Copy all mappings from the specified scope to this scope, overwriting duplicates with mappings from the other scope
    private void putAll(Scope other){
        variableTable.putAll(other.getVTable());
        functionTable.putAll(other.getFTable());
    }

    //clones all the stuff from a scope except for the declaredTable(!)
    @Override
    public Scope clone(){
        Scope clonedScope = new Scope();
        clonedScope.putAll(this);
        return clonedScope;
    }

    public void declareVariable(String identifier, SymbolTableEntry variable){
        if (declaredTable.contains(identifier)) {
            throw new VariableAlreadyDefinedException("The variable name \"" + identifier + "\" is already in use!");
        } else {
            variableTable.put(identifier, variable);
            declaredTable.add(identifier);
        }
    }

    public void declareState(String identifier){
        if(stateTable.contains(identifier)){
            throw new StateAlreadyDefinedExpection();
        } else {
            declaredTable.add(identifier);
        }
    }

    //creates a new scope with all the mappings of the origin scope, but with the declaredTable reset so variables can be overwritten by new declarations
    public static Scope open(Scope origin){
        Scope newScope = origin.clone();
        return newScope;
    }
}