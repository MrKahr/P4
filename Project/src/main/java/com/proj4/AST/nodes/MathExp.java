package com.proj4.AST.nodes;

public class MathExp extends Expression{
    //Field
    private MathExpOperator operator;
    private Integer constant;

    //Constructor
    public MathExp(MathExpOperator operator, Integer constant){
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
