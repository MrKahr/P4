package com.proj4.AST.nodes;

public class ForLoop extends Statement{
    //Constructor
    public ForLoop(Identifiable iterator, Expression condition, Assignment iteratorAction, Body body){
        addChild((AST) iterator);    //first child is the iterator
        addChild(condition);        //second child is the condition
        addChild(iteratorAction);   //third child is what to do with the iterator every loop
        for (AST child : body.getChildren()){   //add extra children from a potential body node
            addChild(child);
        }
    }

    //Method
    public Identifiable getIterator(){                      //using Identifiable here instead of PrimitiveDecl 
        return (Identifiable) getChildren().get(0);   //since we just need to be able to refer to
    }                                                       //the identifier for the loop to work, in theory

    public Expression getCondition(){
        return (Expression) getChildren().get(1);
    }

    public Assignment getIteratorAction(){
        return (Assignment) getChildren().get(2);
    }

    public Body getBody(){
        return (Body) getChildren().get(3);
    }
}
