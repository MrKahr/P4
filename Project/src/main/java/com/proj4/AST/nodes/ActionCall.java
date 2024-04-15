package com.proj4.AST.nodes;

public class ActionCall extends Statement implements Identifiable{
    //Field
    private String identifier;
    

    //Constructor
    //left as default

    //Method
    public String getIdentifier(){
        return identifier;
    }
    
}
