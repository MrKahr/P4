package com.proj4.AST.nodes;

public class BoolExp extends Expression{
    //Field
    private BoolExpOperator operator;
    private Boolean constant;

    //Constructor
    public BoolExp(BoolExpOperator operator, Boolean constant){
        this.operator = operator;
        this.constant = constant;
    }

    // Method
    public BoolExpOperator getOperator() {
        return operator;
    }

    public Boolean getConstant() {
        return constant;
    }
}
