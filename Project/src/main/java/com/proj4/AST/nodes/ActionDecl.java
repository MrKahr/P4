package com.proj4.AST.nodes;

public class ActionDecl extends AST implements Identifiable, Typed{
    //Field
    private String complexReturnType;
    private String returnType;
    private String identifier;
    private AST body;
    private Integer nestingLevel = 0;

    //Constructor
    public ActionDecl(String identifier, String returnType, String complexReturnType, Body body, int nestingLevel){
        this.identifier = identifier;
        this.returnType = returnType;
        this.complexReturnType = complexReturnType;
        this.body = body;
        this.nestingLevel = nestingLevel;
    }

    public ActionDecl(String identifier, Body body){
        this.identifier = identifier;
    }

    //Method
    public String getIdentifier(){
        return identifier;
    }

    public String getType(){
        return returnType;
    }
    
    public String getComplexReturnType(){
        return complexReturnType;
    }

    public AST getBody(){
        return body;
    }

    public String getComplexType(){
        return "Action";
    }

    public Integer getNestingLevel(){
        return this.nestingLevel;
    }
}