package com.proj4.symbolTable.symbols;

import java.util.ArrayList;

import com.proj4.AST.nodes.Body;
import com.proj4.symbolTable.Scope;

public class ActionSymbol{
    //Field
    private ArrayList<String> parameterNames = new ArrayList<>();
    private Body body;
    private String returnType;
    private String complexReturnType;
    private int nestingLevel;
    private Scope initialScope;

    //Constructor
    public ActionSymbol(String returnType, String complexReturnType, Body body, int nestingLevel){    //content list is created by default
        this.returnType = returnType;
        this.body = body;
        this.complexReturnType = complexReturnType;
        this.nestingLevel = nestingLevel;
    }    

    //Method
    public ArrayList<String> getParameterNames(){
        return parameterNames;
    }

    public Body getBody() {
        return body;
    }

    public String getReturnType(){
        return returnType;
    }

    public String getComplexReturnType(){
        return complexReturnType;
    }

    public void setParameterNames(ArrayList<String> content){
        parameterNames = content;
    }

    public void addParameterName(String entry){
        parameterNames.add(entry);
    }

    //TODO: probably not needed. Look into it if there's time
    public String getComplexType(){
        return "Action";
    }

    public int getNestingLevel(){
        return nestingLevel;
    }

    public Scope getInitialScope(){
        return initialScope;
    }

    public void setInitialScope(Scope scope){
        initialScope = scope;
    }
}
