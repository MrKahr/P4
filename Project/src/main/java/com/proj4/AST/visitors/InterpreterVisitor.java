package com.proj4.AST.visitors;

import com.proj4.symbolTable.symbols.SymbolTableEntry;

//this class is a singleton
public class InterpreterVisitor{

    //Field
    private static InterpreterVisitor instance;

    private SymbolTableEntry returnSymbol;   //proof of concept for a tool for the interpreter: This field holds the most recently computed value
    private Boolean allowInterpreting = true;//this flag should block interpreting if false. Useful for action and rule declarations
    private String currentState;
    private String currentActionIdentifier;            //if we're inside an action body, this will hold the action's name

    //Constructor
    private InterpreterVisitor(){
        
    }

    //Method
    public static InterpreterVisitor getInstance(){
        if (instance == null) {
            instance = new InterpreterVisitor();
        }
        return instance;
    }

    public void setReturnSymbol(SymbolTableEntry symbol){
        returnSymbol = symbol;
    }

    public void setCurrentState(String newState){
        currentState = newState;
    }

    public void setAllowInterpreting(Boolean allow){
        allowInterpreting = allow;
    }

    public SymbolTableEntry getReturnSymbol(){
        return returnSymbol;
    }

    public Boolean interpretingAllowed(){
        return allowInterpreting;
    }

    public String getCurrentState(){
        return currentState;
    }

    public String getCurrentActionIdentifier(){
        return currentActionIdentifier;
    }

    public void setCurrentAction(String actionName){
        currentActionIdentifier = actionName;
    }

}
