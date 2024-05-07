package com.proj4.symbolTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

import com.proj4.exceptions.VariableAlreadyDefinedException;
import com.proj4.symbolTable.symbols.TemplateSymbol;
import com.proj4.symbolTable.symbols.ActionSymbol;
import com.proj4.symbolTable.symbols.RuleSymbol;
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
    private static HashMap<String, RuleSymbol> stateTable = new HashMap<>();    //TODO: stateTable contains rulesymbols for now 
    // this table keeps track of which rules have been declared 
    private static HashMap<String, ArrayList<RuleSymbol>> ruleTable = new HashMap<>();

    //this table keeps track of variables
    private HashMap<String, SymbolTableEntry> variableTable = new HashMap<>();

    private HashSet<String> declaredTable = new HashSet<>();   //this table keeps track of whether or not a variable or function has been declared in this scope

    // TODO: create hashmap of observers instead of arrayList
    private static ArrayList<InterpreterObserver> currentObservers = new ArrayList<InterpreterObserver>();

    private static boolean inDebugMode = false;

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

    public static HashMap<String, RuleSymbol> getStateTable(){
        return stateTable;
    }

    public static HashMap<String, ArrayList<RuleSymbol>> getRuleTable(){
        return ruleTable;
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

    public static void setStateTable(HashMap<String, RuleSymbol> table){
        stateTable = table;
    }

    public static void setRuleTable(HashMap<String, ArrayList<RuleSymbol>> table){
        ruleTable = table;
    }

    //use this method to bind a rule to an action
    public static void declareRule(String triggerAction, RuleSymbol ruleSymbol){
        if (ruleTable.get(triggerAction) == null) { //assuming we don't need to check if the arraylist is empty
            ruleTable.put(triggerAction, new ArrayList<RuleSymbol>());
        }
        ruleTable.get(triggerAction).add(ruleSymbol);
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
            //TODO: remove debug print
            System.out.println("Declaring variable \"" + identifier + "\" with type \"" + variable.getType() + "\", complex type \"" + variable.getComplexType() + "\".");
            variableTable.put(identifier, variable);
            declaredTable.add(identifier);
        }
    }
// TODO: states are unimplemented right now
    public void declareState(String identifier){
        /*
        if(stateTable.contains(identifier)){
            throw new StateAlreadyDefinedExpection();
        } else {
            stateTable.add(identifier);
        }
        */
    }
    //creates a new scope with all the mappings of the origin scope, but with the declaredTable reset so variables can be overwritten by new declarations
    public static Scope open(Scope origin){
        Scope newScope = origin.clone();
        return newScope;
    }

    // Stack method wrappers 
    public static void exit(){
        // We save the current scope if we want to use the scope in testing or debugging 
        if(inDebugMode){
            notifyObservers(Scope.getScopeStack());
        }
        scopeStack.pop();
        
    }

    public static void enter(){
        scopeStack.push(new Scope());
            // We save the current scope if we want to use the scope in testing or debugging 
            if(inDebugMode){
                notifyObservers(Scope.getScopeStack());
            }
    }

    // Scopes inherited are pushed unto the stack because the stack top models the current available scope. 
    public static void inherit(){
        if (scopeStack.empty()) {
            throw new NullPointerException("Cannot inherit scope - Stack is empty");
        }
        scopeStack.push(scopeStack.peek().clone()); //clone the top scope and push it

            // We save the current scope if we want to use the scope in testing or debugging 
            if(inDebugMode){
                notifyObservers(Scope.getScopeStack());
            }
    }

    public static void synthesize(){
        Scope poppedScope = scopeStack.pop();
        Scope currentScope = scopeStack.peek();

        // We save the current scope if we want to use the scope in testing or debugging 
        if(inDebugMode){
            notifyObservers(Scope.getScopeStack());
        }

        for (String identifier : poppedScope.getVariableTable().keySet()) {
            if(!poppedScope.getDeclaredTable().contains(identifier)){
                currentScope.getVariableTable().put(identifier, poppedScope.getVariableTable().get(identifier));
            }
        }
    }

    public static Stack<Scope> getScopeStack(){
        return scopeStack;
    }

    public static Scope getCurrent(){
        return scopeStack.peek();
    }

    // OBSERVER PART 
    private static void addObserver(InterpreterObserver interpreterObserver){
        currentObservers.add(interpreterObserver);
    }

    private static void removeObserver(){
        currentObservers.remove(-1);
    }

    private static void notifyObservers(Stack<Scope> currentScope){
        for (InterpreterObserver interpreterObserver : currentObservers) {
            interpreterObserver.setCurrentScope(currentScope);
        }
    }

    private static void setDebugStatus(Boolean truthValue){
        inDebugMode = truthValue;
    }
}