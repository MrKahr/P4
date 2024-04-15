package com.proj4.AST.nodes;

public class IfElse extends Statement{
    //Field

    //Constructor
    public IfElse(BoolExp condition, Statement thenBlock, Statement elseBlock){
        addChild(condition);         //first child is the condition
        addChild((AST)thenBlock);    //second child is to be executed when condition is true
        addChild((AST)elseBlock);    //third cild is to be executed when condition is false
    }

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
