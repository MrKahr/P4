package com.proj4.AST.nodes;

import java.util.ArrayList;

//this class represents the indexing of a template like template.field or card.number
//we can treat the template's field as indeces in an array-like structure where entries don't have the same size
public class TemplateAccess extends Expression implements Identifiable{
    //Field
    private String identifier;  //the identity that the template is bound to in the symbol table
    private ArrayList<String> path;

    //Constructor
    public TemplateAccess(String identifier, ArrayList<String> path){
        this.identifier = identifier;
        this.path = path;
    }
    
    //Method
    public String getIdentifier(){
        return identifier;
    }

    public ArrayList<String> getPath(){
        return this.path;
    }

    //adds another field to the path
    public void extendPath(String field){
        path.add(field);
    }
}
