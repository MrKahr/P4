package com.proj4.AST.nodes;

public class ActionDecl extends AST implements Identifiable, Typed{
    //Field
    private String complexReturnType;
    private String returnType;
    private String identifier;
    private AST body;

    //Constructor
    public ActionDecl(String identifier, String returnType, String complexReturnType, AST body){
        this.identifier = identifier;
        this.returnType = returnType;
        this.complexReturnType = complexReturnType;
        this.body = body;
    }
    public ActionDecl(String identifier){
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
}