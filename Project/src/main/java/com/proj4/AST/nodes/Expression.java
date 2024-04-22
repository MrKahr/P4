package com.proj4.AST.nodes;

import com.proj4.symbolTable.symbols.PrimitiveSymbol;

//Expressions can have up to two operands (which are also expressions)
//note that one or both operands may be null if the operator type is CONSTANT or IDENTIFIER
public class Expression extends Statement{
    
    //Field
    private ExpressionOperator operator;
    @SuppressWarnings("rawtypes")   //compiler complains about unspecified generics, but it's fine because we check the type ourselves
    private PrimitiveSymbol constant;

    //Constructor
    public Expression(){} //constructor with nothing, required for Variable.java to extend this class

    public Expression(ExpressionOperator operator, @SuppressWarnings("rawtypes") PrimitiveSymbol constant){
        this.operator = operator;
        this.constant = constant;
    }

    public Expression(ExpressionOperator operator, Expression firstOperand, Expression secondOperand){
        this.operator = operator;
        addChild(firstOperand);
        addChild(secondOperand);
        //constant field is untouched and becomes null
    }    


    //Method
    public ExpressionOperator getOperator(){
        return operator;
    }

    @SuppressWarnings("rawtypes")
    public PrimitiveSymbol getConstant(){
        return constant;
    }

    public Expression getFirstOperand(){
        return (Expression) getChildren().get(0);
    }

    public Expression getSecondOperand(){
        return (Expression) getChildren().get(1);
    }
}
