package com.proj4.AST.nodes;

public class ForLoop extends Statement{
    private Identifiable iterator;
    private Expression condition;
    //Constructor
    public ForLoop(Identifiable iterator, Expression condition, Assignment iteratorAction, Body body){
        this.iterator = iterator;
        this.condition = condition;
        for (AST child : body.getChildren()){   //add extra children from a potential body node
            addChild(child);
        }
        addChild(iteratorAction);   //last child is what to do with the iterator every loop
    }

    //Method
    public Identifiable getIterator(){                      //using Identifiable here instead of PrimitiveDecl 
        return iterator;   //since we just need to be able to refer to
    }                                                       //the identifier for the loop to work, in theory

    public Expression getCondition(){
        return condition;
    }

    public Assignment getIteratorAction(){
        return (Assignment) getChildren().get(1);
    }
}
