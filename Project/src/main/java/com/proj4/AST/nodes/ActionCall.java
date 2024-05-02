package com.proj4.AST.nodes;

import com.proj4.symbolTable.symbols.PrimitiveSymbol;

public class ActionCall extends Expression implements Identifiable{
    //Field
    private String identifier;

    public ActionCall(String identifier){
        this.identifier = identifier;   //which action to call
    }

    public String getIdentifier(){
        return this.identifier;
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
