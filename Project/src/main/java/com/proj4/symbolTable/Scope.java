package com.proj4.symbolTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

import com.proj4.exceptions.StateAlreadyDefinedExpection;
import com.proj4.exceptions.VariableAlreadyDefinedException;
import com.proj4.symbolTable.symbols.TemplateSymbol;
import com.proj4.symbolTable.symbols.ActionSymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

//this class represents a scope in the programming language
public class Scope implements Cloneable{
    //Field
    private static Stack<Scope> scopeStack = new Stack<>();

    //this table keeps track of template blueprints and their default values, to be used when instantiating them
    private static HashMap<String, TemplateSymbol> blueprintTable = new HashMap<>();
    //this table keeps a map of every declared template in order to properly index them
    private static HashMap<String, ArrayList<String>> templateMapTable = new HashMap<>();
    //this table keeps track of actions
    private static HashMap<String, ActionSymbol> actionTable = new HashMap<>();
    //this table keeps track of which states have been declared in the current scope
    private static HashSet<String> stateTable = new HashSet<>(); 

    //this table keeps track of variables
    private HashMap<String, SymbolTableEntry> variableTable = new HashMap<>();

    private HashSet<String> declaredTable = new HashSet<>();   //this table keeps track of whether or not a variable or function has been declared in this scope

    
    //Method
    public HashMap<String, SymbolTableEntry> getVariableTable(){
        return variableTable;
    }

    public static HashMap<String, ActionSymbol> getActionTable(){
        return actionTable;
    }

    public static HashMap<String, TemplateSymbol> getBlueprintTable(){
        return blueprintTable;
    }

    public static HashMap<String, ArrayList<String>> getTemplateMapTable(){
        return templateMapTable;
    }

    public HashSet<String> getDeclaredTable(){
        return declaredTable;
    }

    public static HashSet<String> getStateTable(){
        return stateTable;
    }

    public void setVariableTable(HashMap<String, SymbolTableEntry> table){
        variableTable = table;
    }

    public static void setActionTable(HashMap<String, ActionSymbol> table){
        actionTable = table;
    }
    
    public static void setBlueprintTable(HashMap<String, TemplateSymbol> table){
        blueprintTable = table;
    }

    public void setDeclaredTable(HashSet<String> table){
        declaredTable = table;
    }

    public static void setStateTable(HashSet<String> table){
        stateTable = table;
    }


    //Copy all mappings from the specified scope to this scope, overwriting duplicates with mappings from the other scope
    public void putAll(Scope other){
        variableTable.putAll(other.getVariableTable());
        //declaredTable.addAll(other.getDTable());  <- don't do this! Otherwise variables must have unique names always
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
            stateTable.add(identifier);
        }
    }

    //creates a new scope with all the mappings of the origin scope, but with the declaredTable reset so variables can be overwritten by new declarations
    public static Scope open(Scope origin){
        Scope newScope = origin.clone();
        return newScope;
    }

    // Stack method wrappers 
    public static void exit(){
        scopeStack.pop();
    }

    public static void enter(){
        scopeStack.push(new Scope());
    }

    // Scopes inherited are pushed unto the stack because the stack top models the current available scope. 
    public static void inherit(){
        if (scopeStack.empty()) {
            throw new NullPointerException("Cannot inherit scope - Stack is empty");
        }
        scopeStack.push(scopeStack.peek().clone()); //clone the top scope and push it
    }

    public static void synthesize(){
        Scope poppedScope = scopeStack.pop();
        scopeStack.peek().putAll(poppedScope);
    }

    public static Stack<Scope> getScopeStack(){
        return scopeStack;
    }

    public static Scope getCurrent(){
        return scopeStack.peek();
    }
}