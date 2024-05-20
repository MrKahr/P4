package com.proj4.symbolTable;

import java.util.ArrayList;
import java.util.Stack;

//this class manages the stack of scopes in a DBL-program
public class ScopeManager {
    //Field
    //this class is a singleton:
    private static ScopeManager singleInstance;

    // Flags whether the interpreter should copy scopes to its observers //TODO: Use debug flag to restrict interpreter's printing   
    private static boolean inDebugMode = false;


    //Stack of scopes
    private Stack<Scope> scopeStack = new Stack<>();
    
    // TODO: create hashmap of observers instead of arrayList
    private static ArrayList<InterpreterObserver> currentObservers = new ArrayList<InterpreterObserver>();


    //Constructor
    private ScopeManager(){

    }

    //Method
    //get the instance of this class and create it if it doesn't exist yet
    public static ScopeManager getInstance(){
        if (singleInstance == null) {
            singleInstance = new ScopeManager();
        }
        return singleInstance;
    }

    // Stack method wrappers
    public void exit(){
        // We save the current scope if we want to use the scope in testing or debugging
        if(inDebugMode){
            notifyObservers(copyStack());
        }
        scopeStack.pop();
    }

    public void enter(){
        scopeStack.push(new Scope());
            // We save the current scope if we want to use the scope in testing or debugging
            if(inDebugMode){
                notifyObservers(copyStack());
            }
    }

    // Scopes inherited are pushed unto the stack because the stack top models the current available scope.
    public void inherit(){
        if (scopeStack.empty()) {
            throw new NullPointerException("Cannot inherit scope - Stack is empty");
        }
        scopeStack.push(scopeStack.peek().clone()); //clone the top scope and push it

            // We save the current scope if we want to use the scope in testing or debugging
            if(inDebugMode){
                notifyObservers(copyStack());
            }
    }

    public void synthesize(){
        Scope poppedScope = scopeStack.pop();
        Scope currentScope = scopeStack.peek();

        // We save the current scope if we want to use the scope in testing or debugging
        if(inDebugMode){
            notifyObservers(copyStack());
        }

        for (String identifier : poppedScope.getVariableTable().keySet()) {
            if(!poppedScope.getDeclaredTable().contains(identifier)){
                currentScope.getVariableTable().put(identifier, poppedScope.getVariableTable().get(identifier));
            }
        }
    }

    public Stack<Scope> getScopeStack(){
        return scopeStack;
    }

    public Scope getCurrent(){
        return scopeStack.peek();
    }

    public void setDebugStatus(Boolean truthValue){
        inDebugMode = truthValue;
    }

    // OBSERVER PART
    public void addObserver(InterpreterObserver interpreterObserver){
        currentObservers.add(interpreterObserver);
    }
    
    public void removeObserver(){
        currentObservers.remove(-1);
    }

    public Stack<Scope> copyStack(){
        Stack <Scope> stackCopy = new Stack<Scope>();
        stackCopy.addAll(scopeStack);
        return stackCopy;
    }

    public void notifyObservers(Stack<Scope> currentScope){
        for (InterpreterObserver interpreterObserver : currentObservers) {
            interpreterObserver.setCurrentScope(currentScope);
        }
    }
}
