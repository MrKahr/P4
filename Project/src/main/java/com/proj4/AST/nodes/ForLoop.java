package com.proj4.AST.nodes;

public class ForLoop extends Statement{
    //Field


    //Constructor
    public ForLoop(Identifiable iterator, BoolExp condition, Assignment iteratorAction, Statement loopBlock){
        addChild((AST)iterator);    //first child is the iterator
        addChild(condition);        //second child is the condition
        addChild(iteratorAction);   //third child is what to do with the iterator every loop
        addChild(loopBlock);        //fourth child is the loop itself. More can be added as children later but at least one is required
    }                               //TODO: should a loopBlock be required?

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
}
