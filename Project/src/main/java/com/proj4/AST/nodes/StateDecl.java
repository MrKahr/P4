package com.proj4.AST.nodes;

import java.util.ArrayList;

//technically a declaration, but shouldn't be usable in places where other declarations are
//this one also shouldn't have any child nodes, since it just says to store the identifier in a special symbol table for states
public class StateDecl extends AST implements Identifiable{
    //Field
    private String identifier;
    private ArrayList<String> actionList; 

    //Constructor
    public StateDecl(String identifier){
        this.identifier = identifier;
        this.actionList = new ArrayList<String>();
    }

    //Method
    public String getIdentifier(){
        return identifier;
    }

    public ArrayList<String> getActionList(){
        return this.actionList;
    };
}
