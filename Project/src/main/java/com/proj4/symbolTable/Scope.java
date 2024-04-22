package com.proj4.symbolTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.proj4.exceptions.StateAlreadyDefinedExpection;
import com.proj4.exceptions.VariableAlreadyDefinedException;
import com.proj4.symbolTable.symbols.ComplexSymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

//this class represents a scope in the programming language
public class Scope implements Cloneable{
    //Field

    //this table keeps track of template blueprints and their default values, to be used when instantiating them
    private static HashMap<String, ComplexSymbol> blueprintTable = new HashMap<>();
    //this table keeps a map of every declared template in order to properly index them
    private static HashMap<String, ArrayList<String>> templateTable = new HashMap<>();

    //this table keeps track of variables
    private HashMap<String, SymbolTableEntry> variableTable = new HashMap<>();
    //this table keeps track of actions
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

    public HashMap<String, ComplexSymbol> getBTable(){
        return blueprintTable;
    }

    public HashMap<String, ArrayList<String>> getTTable(){
        return templateTable;
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
    
    public void setBTable(HashMap<String, ComplexSymbol> table){
        blueprintTable = table;
    }

    public void setDTable(HashSet<String> table){
        declaredTable = table;
    }

    public void setStateTabel(HashSet<String> table){
        stateTable = table;
    }


    //Copy all mappings from the specified scope to this scope, overwriting duplicates with mappings from the other scope
    public void putAll(Scope other){
        variableTable.putAll(other.getVTable());
        functionTable.putAll(other.getFTable());
        blueprintTable.putAll(other.getBTable());
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