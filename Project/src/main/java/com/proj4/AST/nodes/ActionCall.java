package com.proj4.AST.nodes;

public class ActionCall extends Expression {
    private String identifier;

    public ActionCall(String identifier){
        this.identifier = identifier;
    }

    public String getIdentifier(){
        return this.identifier;
    }
}
