package com.proj4.AST.nodes;

public class MathExp extends Expression implements Identifiable{
    //Field
    private MathExpOperator operator;
    private String identifier;
    private int constant;

    //Constructor
    public MathExp(MathExpOperator operator, String identifier, int constant){
        this.operator = operator;
        this.identifier = identifier;
        this.constant = constant;
    }

    //Method
    public MathExpOperator getOperator(){
        return operator;
    }

    public String getIdentifier(){
        return identifier;
    }

    public int getConstant(){
        return constant;
    }
}
