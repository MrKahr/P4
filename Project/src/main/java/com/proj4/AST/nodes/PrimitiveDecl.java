package com.proj4.AST.nodes;

public class PrimitiveDecl extends Declaration{
    //Field
    private String identifier;
    private String expectedType;    //the type this variable should have

    //Constructor
    public PrimitiveDecl(String identifier, String expectedType){
        this.identifier = identifier;
        this.expectedType = expectedType;
    }

    //Method
    public String getIdentifier(){
        return identifier;
    }

    public String getExpectedType(){
        return expectedType;
    }
}
