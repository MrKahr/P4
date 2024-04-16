package com.proj4.AST.nodes;

public class ForLoop extends Statement{
    //Field


    //Constructor
    public ForLoop(Identifiable iterator, BoolExp condition, Assignment iteratorAction, Statement loopBlock){
        addChild((AST) iterator);    //first child is the iterator
        addChild(condition);        //second child is the condition
        addChild(iteratorAction);   //third child is what to do with the iterator every loop
        addChild((AST) loopBlock);   //fourth child is the loop itself
    }

    //Method
    public Identifiable getIterator(){                      //using Identifiable here instead of PrimitiveDecl 
        return (Identifiable) getChildren().get(0);   //since we just need to be able to refer to
    }                                                       //the identifier for the loop to work, in theory

    public BoolExp getCondition(){
        return (BoolExp) getChildren().get(1);
    }

    public AST getIteratorAction(){
        return (AST) getChildren().get(2);
    }

    public Statement getLoopBlock(){
        return (Statement) getChildren().get(3);
    }
}
