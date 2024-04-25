package com.proj4.AST.nodes;

//this class represents an identifier whose value should be returned.
//it is an expression, so it can also be used in assignments, and as an operand for other expressions.
//by not having this built into the other expressions, we can use it to pass data structures like templates and arrays around
//for example to use them as arguments in actions or assigning variables to them.
//in other words, if "x" is a template or array, we can now write var IS x, if "var" has the same type, of course.
public class Variable extends Expression implements Identifiable{
    //Field
    private String identifier;
    private String complexType;

    //Constructor
    public Variable(String identifier, String complexType){
        this.identifier = identifier;
        this.complexType = complexType;
    }

    //Method
    public String getIdentifier(){
        return identifier;
    }
    public String getComplexType(){
        return complexType;
    }

    @Override
    public Expression getFirstOperand(){
        return null;
    }

    @Override
    public Expression getSecondOperand(){
        return null;
    }

}
