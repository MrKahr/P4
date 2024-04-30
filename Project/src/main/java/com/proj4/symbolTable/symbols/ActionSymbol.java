package com.proj4.symbolTable.symbols;

import java.util.ArrayList;

import com.proj4.AST.nodes.AST;

public class ActionSymbol extends ComplexSymbol{
    //Field
    private ArrayList<SymbolTableEntry> params = new ArrayList<>();
    private AST body;
    private String complexReturnType;
    private int nestingLevel;

    //Constructor
    public ActionSymbol(String returnType, String complexReturnType, AST body, int nestingLevel){    //content list is created by default
        setType(returnType);
        this.body = body;
        this.complexReturnType = complexReturnType;
        this.nestingLevel = nestingLevel;
    }    

    //Method
    public ArrayList<SymbolTableEntry> getContent(){
        return params;
    }

    public AST getBody() {
        return body;
    }

    public String getComplexReturnType(){
        return complexReturnType;
    }

    public void setContent(ArrayList<SymbolTableEntry> content){
        params = content;
    }

    public void addContent(SymbolTableEntry entry){
        params.add(entry);
    }

    public String getComplexType(){
        return "Action";
    }

    public int getNestingLevel(){
        return nestingLevel;
    }
}
