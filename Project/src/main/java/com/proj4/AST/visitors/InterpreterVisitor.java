package com.proj4.AST.visitors;

import com.proj4.symbolTable.symbols.SymbolTableEntry;

public abstract class InterpreterVisitor implements NodeVisitor {

    //Field
    private static SymbolTableEntry returnSymbol;   //proof of concept for a tool for the interpreter: This field holds the most recently computed value
    private static Boolean allowInterpreting = true;//this flag should block interpreting if false. Useful for action and rule declarations
    private static String currentState;
    private static String currentActionIdentifier;            //if we're inside an action body, this will hold the action's name

    public static void setReturnSymbol(SymbolTableEntry symbol){
        returnSymbol = symbol;
    }

    public static void setCurrentState(String newState){
        currentState = newState;
    }

    public static void setAllowInterpreting(Boolean allow){
        allowInterpreting = allow;
    }

    public static SymbolTableEntry getReturnSymbol(){
        return returnSymbol;
    }

    public static Boolean interpretingAllowed(){
        return allowInterpreting;
    }

    public static String getCurrentState(){
        return currentState;
    }

    public static String getCurrentActionIdentifier(){
        return currentActionIdentifier;
    }

    public static void setCurrentAction(String actionName){
        currentActionIdentifier = actionName;
    }
}
