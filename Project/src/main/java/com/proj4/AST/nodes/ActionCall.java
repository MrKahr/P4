package com.proj4.AST.nodes;

import com.proj4.symbolTable.symbols.PrimitiveSymbol;

public class ActionCall extends Expression implements Identifiable{
    //Field
    private String identifier;

    //Constructor
    public ActionCall(String identifier){
        this.identifier = identifier;   //which action to call
    }

    //Method
    public String getIdentifier(){
        return identifier;
    }
    
    @Override
    public ExpressionOperator getOperator(){
        return null;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public PrimitiveSymbol getConstant(){
        return null;
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
