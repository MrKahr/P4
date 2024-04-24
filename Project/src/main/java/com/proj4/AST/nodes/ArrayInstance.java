package com.proj4.AST.nodes;

public class ArrayInstance extends Expression implements Identifiable, Typed{
    //Field
    private String type;
    private String identifier;
    
    //Constructor
    public ArrayInstance(String identifier, String type){
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

    public String getComplexType(){
        return "Array";
    }
}
//TODO: Note: An array instance is something like "[e_0,...,e_n]" where all e's are expressions with n>=0;