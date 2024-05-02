package com.proj4.AST.nodes;

public class TemplateInstance extends Expression {
    //Field
    private String instanceType;//what type of template this instance is

    //Constructor
    public TemplateInstance(String instanceType){
        this.instanceType = instanceType;
    }

    //Method
    public String getType(){
        return instanceType;
    }

    public String getComplexType(){
        return "Template";
    }
}
//TODO: Note: A template instance is something like "NEW card" where "card" is a template type. 