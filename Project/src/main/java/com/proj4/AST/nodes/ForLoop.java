package com.proj4.AST.nodes;

public class ForLoop extends Statement{
    private Identifiable iterator;
    private Expression condition;
    private Assignment iteratorAction;
    //Constructor
    public ForLoop(Identifiable iterator, Expression condition, Assignment iteratorAction, Body body){
        this.iterator = iterator;
        this.condition = condition;
        this.iteratorAction = iteratorAction;
        for (AST child : body.getChildren()){   //add extra children from a potential body node
            addChild(child);
        }
    }

    //Method
    public Identifiable getIterator(){                      //using Identifiable here instead of PrimitiveDecl
        return iterator;   //since we just need to be able to refer to
    }                                                       //the identifier for the loop to work, in theory

    public Expression getCondition(){
        return condition;
    }

    public Assignment getIteratorAction(){
        return this.iteratorAction;
    }
}
