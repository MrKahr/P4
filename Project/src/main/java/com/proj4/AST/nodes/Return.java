package com.proj4.AST.nodes;

import com.proj4.exceptions.MalformedAstException;

//TODO: a bit iffy on whether or not this one should be part of the AST,
//TODO: but I think it makes sense to have some way to mark what should be returned
public class Return extends Statement{

    //Constructor
    public Return(Expression returnValue){
        addChild(returnValue);
    }

    //Method
    public Expression getReturnValue(){
        try {
            return (Expression) getChild(0);
        } catch (ClassCastException cce) {
            throw new MalformedAstException("Expected to find Expression as child of Return but found \"" + getChild(0).getClass().getSimpleName() + "\"");
        }
    }
}
