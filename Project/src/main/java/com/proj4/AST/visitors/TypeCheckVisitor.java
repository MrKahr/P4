package com.proj4.AST.visitors;

import com.proj4.symbolTable.symbols.SymbolTableEntry;

public abstract class TypeCheckVisitor implements NodeVisitor {

    //Field
    private static String foundType;    //this holds the most recent type we have found. Used to circumvent visitors not being able to return anything
    private static String foundComplexType;  //this holds the most recent complex type we have found. Used for differentiating between arrays, templates, and primitives
    private static String currentAction;//if we're inside an action body, this will hold the action's name
    
    private static SymbolTableEntry lastSymbol; //proof of concept for a tool for the interpreter: This field holds the most recently computed value

    //Method
    public static String getFoundType(){
        return foundType;
    }

    public static String getFoundComplexType(){
        return foundComplexType;
    }

    public static String getCurrentAction(){
        return currentAction;
    }

    public static void setFoundType(String type, String complexType){
        foundType = type;
        foundComplexType = complexType;
    }
    
    public static void setCurrentAction(String actionName){
        currentAction = actionName;
    }

}
