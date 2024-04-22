package com.proj4.AST.nodes;

//Expressions can have up to two operands (which are also expressions)
//note that one or both operands may be null if the operator type is CONSTANT or IDENTIFIER
public abstract class Expression extends Statement{
    
    public Expression getFirstOperand(){
        return (Expression) getChildren().get(0);
    }

    public Expression getSecondOperand(){
        return (Expression) getChildren().get(1);
    }
}
