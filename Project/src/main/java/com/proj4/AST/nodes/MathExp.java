package com.proj4.AST.nodes;

public class MathExp extends Expression implements Identifiable{
    //Field
    private MathExpOperator operator;
    private String identifier;
    private Integer constant;

    //Constructor
    public MathExp(MathExpOperator operator, String identifier, Integer constant){
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
