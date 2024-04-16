package com.proj4.AST.nodes;

public class ForOf extends Statement implements Identifiable {
    //Field

    private String identifier;

    //Constructor
    public ForOf(String identifier, Statement statement){
        this.identifier = identifier;
        addChild((AST) statement);
    }
    
    //Method
    public String getIdentifier() {
       return identifier;
    }

    public Statement getStatement(){
        return (Statement) getChildren().get(0);
    }
}
