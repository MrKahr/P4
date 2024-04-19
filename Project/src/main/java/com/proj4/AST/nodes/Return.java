package com.proj4.AST.nodes;

//TODO: a bit iffy on whether or not this one should be part of the AST,
//TODO: but I think it makes sense to have some way to mark what should be returned
public class Return extends Statement{
    //Field


    //Constructor
    public Return(Expression returnValue){
        addChild(returnValue);
    }

    //Method
    public Expression getReturnValue(){
        return (Expression) getChildren().get(0);
    }

}
