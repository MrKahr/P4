package com.proj4.AST.visitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.MathExp;

class MultExpressionVisitor implements NodeVisitor{
    @Override 
    public void visit(AST node){
        System.out.println("This is the multExpressionVisitor");
    }
}