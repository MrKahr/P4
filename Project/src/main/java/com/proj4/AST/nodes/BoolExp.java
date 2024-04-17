package com.proj4.AST.nodes;

public class BoolExp extends Expression{
    //Field
    private BoolExpOperator operator;
    private boolean constant;

    //Constructor
    public BoolExp(BoolExpOperator operator, boolean constant){
        this.operator = operator;
        this.constant = constant;
    }

    //Method
    public BoolExpOperator getOperator(){
        return operator;
    }

    public boolean getConstant(){
        return constant;
    }
}
