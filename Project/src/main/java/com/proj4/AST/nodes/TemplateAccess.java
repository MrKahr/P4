package com.proj4.AST.nodes;

import java.util.ArrayList;

//this class represents the indexing of a template like template.field or card.number
//we can treat the template's field as indeces in an array-like structure where entries don't have the same size
public class TemplateAccess extends Expression implements Identifiable{
    //Field
    private String identifier;  //the identity that the template is bound to in the symbol table
    private ArrayList<String> path = new ArrayList<>(); //the fields that should be indexed, in the order they are added to this list
    
    //Constructor
    public TemplateAccess(String identifier, MathExp index){
        this.identifier = identifier;
        addChild(index);
    }
    
    //Method
    public String getIdentifier(){
        return identifier;
    }

    public MathExp getIndex(){
        return (MathExp) getChildren().get(0);
    }

    public ArrayList<String> getPath(){
        return path;
    }

    //adds another field to the path
    public void extendPath(String field){
        path.add(field);
    }
}
