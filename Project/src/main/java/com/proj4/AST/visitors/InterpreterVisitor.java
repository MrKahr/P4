package com.proj4.AST.visitors;

import com.proj4.symbolTable.symbols.SymbolTableEntry;

public abstract class InterpreterVisitor implements NodeVisitor{

    //Field
    private static SymbolTableEntry returnSymbol; //proof of concept for a tool for the interpreter: This field holds the most recently computed value
    private static Boolean allowInterpreting = true;   //this flag should block interpreting if false. Useful for action and rule declarations

    public void setReturnSymbol(SymbolTableEntry symbol){
        returnSymbol = symbol;
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
}
