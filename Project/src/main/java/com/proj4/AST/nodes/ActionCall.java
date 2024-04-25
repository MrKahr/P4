package com.proj4.AST.nodes;

public class ActionCall extends Statement implements Identifiable{
    //Field
    private String identifier;

    //Constructor
    public ActionCall(String identifier){
        this.identifier = identifier;   //which action to call
    }

    //Method
    public String getIdentifier(){
        return identifier;
    }
    
}
