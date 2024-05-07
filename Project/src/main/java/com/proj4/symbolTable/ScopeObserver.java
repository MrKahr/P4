package com.proj4.symbolTable;

import java.util.Stack;

public class ScopeObserver implements InterpreterObserver {
    // Field 
    private Stack<Scope> currentScope;
    
    // Constructor 

    // Method 
    @Override 
    public void setCurrentScope(Stack<Scope> stackToCopy){
        this.currentScope = stackToCopy;
    }
}
