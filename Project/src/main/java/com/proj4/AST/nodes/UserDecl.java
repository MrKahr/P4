package com.proj4.AST.nodes;

public class UserDecl extends Declaration{
     //Field
    private String identifier;
    private String expectedType;    //the type this variable should have
    private String complexType;

    //Constructor
    public UserDecl(String identifier, String expectedType){
        this.identifier = identifier;
        this.expectedType = expectedType;
    }

    
    //Method
    public String getIdentifier(){
        return identifier;
    }

    public String getType(){
        return expectedType;
    }
    public String getComplexType(){
        return complexType;
    }
}