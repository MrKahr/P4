package com.proj4.AST.nodes;

public class ActionDecl extends AST implements Identifiable, Typed{
    //Field
    private String complexReturnType;
    private String returnType;
    private String identifier;
    private Body body;
    private Integer nestingLevel = -1;

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
        this.body = body;
        this.returnType = "Null";   //setting these to prevent nullpointerexceptions from calling .equals
        this.complexReturnType = "Null";
        this.nestingLevel = -1;
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

    public Body getBody(){
        return body;
    }

    public String getComplexType(){
        return "Action";
    }

    public Integer getNestingLevel(){
        return this.nestingLevel;
    }
}