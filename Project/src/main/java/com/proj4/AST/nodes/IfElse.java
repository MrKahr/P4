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
        try {
            return (Statement) getChildren().get(2);
        } catch (IndexOutOfBoundsException ioobe) { //there might not be an else-block, so we return null if this makes an exception
            return null;
        }
    }
}
