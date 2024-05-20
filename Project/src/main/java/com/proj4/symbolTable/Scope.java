package com.proj4.symbolTable;

import java.util.HashMap;
import java.util.HashSet;

import com.proj4.exceptions.VariableAlreadyDefinedException;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

//this class represents a scope in the programming language
public class Scope implements Cloneable{
    //Field

    //this table keeps track of variables
    private HashMap<String, SymbolTableEntry> variableTable = new HashMap<>();

    //this table keeps track of whether or not a variable or function has been declared in this scope
    private HashSet<String> declaredTable = new HashSet<>();

    //these strings are not allowed to be used as identifiers anywhere
    //TODO: the parser seemingly already handles this
 /*    private static ArrayList<String> keywords = new ArrayList<>(Arrays.asList("Integer", "Boolean", "String",
    "Action", "Rule", "State", "Template", "IF", "ELSE IF", "ELSE", "FOR", "AND", "OR", "GREATER THAN", "GREATER OR EQUALS",
    "LESS THAN", "LESS OR EQUALS", "EQUALS", "NOT EQUALS", "RESULT", "RESULT IN", "RESULTS IN", "CONTAINS", "ALLOWS","WHEN", "WITH LOOP",
    "IS", "NOT", "NEW")); */
    
 
    //Method
    public HashMap<String, SymbolTableEntry> getVariableTable(){
        return variableTable;
    }
   /*
    public static HashMap<String, ActionSymbol> getActionTable(){
        return actionTable;
    }

    public static HashMap<String, TemplateSymbol> getBlueprintTable(){
        return blueprintTable;
    }

    public static HashMap<String, ArrayList<String>> getTemplateMapTable(){
        return templateMapTable;
    }
 */
    public HashSet<String> getDeclaredTable(){
        return declaredTable;
    }
/*
    public static HashMap<String, StateSymbol> getStateTable(){
        return stateTable;
    }

    public static HashMap<String, ArrayList<RuleSymbol>> getRuleTable(){
        return ruleTable;
    }
 */
    public void setVariableTable(HashMap<String, SymbolTableEntry> table){
        variableTable = table;
    }
/*
    public static void setActionTable(HashMap<String, ActionSymbol> table){
        actionTable = table;
    }

    public static void setBlueprintTable(HashMap<String, TemplateSymbol> table){
        blueprintTable = table;
    }
 */
    public void setDeclaredTable(HashSet<String> table){
        declaredTable = table;
    }
/*
    public static void setStateTable(HashMap<String, StateSymbol> table){
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
 */
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
/*
    public static void declareState(String identifier, StateSymbol stateSymbol){
        stateTable.put(identifier, stateSymbol);
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
            notifyObservers(Scope.copyStack());
        }
        scopeStack.pop();
    }

    public static void enter(){
        scopeStack.push(new Scope());
            // We save the current scope if we want to use the scope in testing or debugging
            if(inDebugMode){
                notifyObservers(Scope.copyStack());
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
                notifyObservers(Scope.copyStack());
            }
    }

    public static void synthesize(){
        Scope poppedScope = scopeStack.pop();
        Scope currentScope = scopeStack.peek();

        // We save the current scope if we want to use the scope in testing or debugging
        if(inDebugMode){
            notifyObservers(Scope.copyStack());
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

    public static ArrayList<String> getInbuiltActions(){
        return inbuiltActions;
    }

    /* public static ArrayList<Strings> getKeywords(){
        return keywords;
    }
     * 
     */
    /*
    // OBSERVER PART
    public static void addObserver(InterpreterObserver interpreterObserver){
        currentObservers.add(interpreterObserver);
    }

    public static void removeObserver(){
        currentObservers.remove(-1);
    }

    public static void notifyObservers(Stack<Scope> currentScope){
        for (InterpreterObserver interpreterObserver : currentObservers) {
            interpreterObserver.setCurrentScope(currentScope);
        }
    }

    public static void setDebugStatus(Boolean truthValue){
        inDebugMode = truthValue;
    }

    public static Stack<Scope> copyStack(){
        Stack <Scope> stackCopy = new Stack<Scope>();
        stackCopy.addAll(scopeStack);
        return stackCopy;
    }
     */
}