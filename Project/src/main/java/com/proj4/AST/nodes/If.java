package com.proj4.AST.nodes;

public class If extends Statement {
    //Field

    //Constructor
    public If(){}

    //Method
    public BoolExp getCondition(){
        return (BoolExp) getChildren().get(0);
    }

    public Statement getThenBlock(){
        return (Statement) getChildren().get(1);
    }

    public Statement getElseBlock(){
        return (Statement) getChildren().get(2);
    }
}
