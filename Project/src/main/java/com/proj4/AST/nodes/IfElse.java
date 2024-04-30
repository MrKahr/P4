package com.proj4.AST.nodes;

public class IfElse extends Statement {
    //Field

    //Constructor
    public IfElse(){}

    //Method
    public Expression getCondition(){
        return (Expression) getChildren().get(0);
    }

    public Statement getThenBlock(){
        return (Statement) getChildren().get(1);
    }

    public Statement getElseBlock(){
        return (Statement) getChildren().get(2);
    }
}
