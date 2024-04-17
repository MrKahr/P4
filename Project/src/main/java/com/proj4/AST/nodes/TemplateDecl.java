package com.proj4.AST.nodes;

public class TemplateDecl extends AST implements Identifiable{
    //Field
    private String identifier;  //The template's type i.e. "card" or "coordinate"

    //Constructor
    public TemplateDecl(String identifier){
        this.identifier = identifier;
    }

    //Method
    public String getIdentifier(){
        return identifier;
    }
}
