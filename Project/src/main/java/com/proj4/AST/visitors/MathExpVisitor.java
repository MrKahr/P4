package com.proj4.AST.visitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.MathExp;

class MathExpressionVisitor implements NodeVisitor{
    @Override 
    public void visit(AST node){
        MathExp mathExp = (MathExp) node;
        System.out.println("Visiting MathExpression");
        System.out.println("Contains " + mathExp.getConstant() + " " + mathExp.getOperator());

    }
}