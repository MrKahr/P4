package com.proj4.AST.nodes;

public class MathExp extends Expression{
    //Field
    private MathExpOperator operator;
    private int constant;

    //Constructor
    public MathExp(MathExpOperator operator, int constant){
        this.operator = operator;
        this.constant = constant;
    }

    //Method
    public MathExpOperator getOperator(){
        return operator;
    }

    public int getConstant(){
        return constant;
    }
}
