package com.proj4.AST.nodes;

public class ParameterNode extends AST {

    //Field
    private String type;
    private String identifier;
    
    //Constructor
    public ParameterNode(String identifier, String type){
        this.identifier = identifier;
        this.type = type;   //the type of the entries in this array. Controls how many bytes are needed per entry
    }

    //Method
    public String getIdentifier(){
        return identifier;
    }

    public String getType(){
        return type;
    }
    
}
