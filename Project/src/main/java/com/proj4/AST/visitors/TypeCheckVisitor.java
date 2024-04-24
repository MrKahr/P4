package com.proj4.AST.visitors;

import com.proj4.symbolTable.symbols.SymbolTableEntry;

public abstract class TypeCheckVisitor implements NodeVisitor {

    //Field
    private static String foundType;    //this holds the most recent type we have found. Used to circumvent visitors not being able to return anything
    private static SymbolTableEntry lastSymbol; //proof of concept for a tool for the interpreter: This field holds the most recently computed value

    //Method
    public static String getFoundType(){
        return foundType;
    }

    public static void setFoundType(String type){
        foundType = type;
    }
}
