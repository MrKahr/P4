package com.proj4.AST.nodes;

public class ActionDecl extends Declaration{
    //Field
    private String returnType;
    private String identifier;

    //Constructor
    public ActionDecl(String identifier, String returnType){
        this.identifier = identifier;
        this.returnType = returnType;
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
}