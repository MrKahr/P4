package com.proj4.AST.nodes;

public class BoolExp extends Expression implements Identifiable {
    // Field
    private BoolExpOperator operator;
    private String identifier;
    private Boolean constant;

    // Constructor
    public BoolExp(BoolExpOperator operator, String identifier, Boolean constant) {
        this.operator = operator;
        this.identifier = identifier;
        this.constant = constant;
    }

    // Method
    public BoolExpOperator getOperator() {
        return operator;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Boolean getConstant() {
        return constant;
    }
}
