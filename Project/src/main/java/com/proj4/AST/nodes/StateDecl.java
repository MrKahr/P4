package com.proj4.AST.nodes;

import java.util.ArrayList;

//technically a declaration, but shouldn't be usable in places where other declarations are
//this one also shouldn't have any child nodes, since it just says to store the identifier in a special symbol table for states
public class StateDecl extends AST implements Identifiable{
    //Field
    private String identifier;
    private ArrayList<String> actionList; //a list of actions the user may choose to start while in this state
    private Body stateBody; //code that executes every time the user is prompted for input

    //Constructor
    public StateDecl(String identifier, ArrayList<String> actionList){
        this.identifier = identifier;
        this.actionList = actionList;
    }

    public StateDecl(String identifier, ArrayList<String> actionList, Body body){
        this.identifier = identifier;
        this.actionList = actionList;
        this.stateBody = body;
    }

    //Method
    public String getIdentifier(){
        return identifier;
    }

    public ArrayList<String> getActionList(){
        return this.actionList;
    }

    public Body getBody(){
        return this.stateBody;
    }
}