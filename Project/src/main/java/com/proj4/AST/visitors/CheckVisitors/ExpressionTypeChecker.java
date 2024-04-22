package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Expression;
import com.proj4.AST.visitors.TypeCheckVisitor;

public class ExpressionTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        Expression expression = (Expression) node;
        expression.inheritScope();
    }
}
