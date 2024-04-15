package com.proj4.AST.nodes;

public class TemplateInstance extends Declaration {
    //Field
    private String identifier;  //what to bind to this instance in the symbol table
    private String instanceType;//what type of template this instance is

    //Constructor
    public TemplateInstance(String identifier, String instanceType){
        this.identifier = identifier;
        this.instanceType = instanceType;
    }

    //Method
    public String getIdentifier(){
        return identifier;
    }

    public String getInstanceType(){
        return instanceType;
    }
}
