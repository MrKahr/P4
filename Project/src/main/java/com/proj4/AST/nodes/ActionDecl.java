package com.proj4.AST.nodes;

public class ActionDecl extends Declaration{
    //Field
    private String returnType;
    private String identifier;

    public ActionDecl(){}

    //Constructor
    public ActionDecl(String identifier, String returnType){
        this.identifier = identifier;
        this.returnType = returnType;
    }

    //Method
    public String getIdentifier(){
        return identifier;
    }

    public String getType(){
        return returnType;
    }
}