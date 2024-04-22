package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.IfElse;
import com.proj4.AST.visitors.TypeCheckVisitor;

public class IfElseTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        IfElse ifElse = (IfElse) node;
        ifElse.inheritScope();
    }
}
