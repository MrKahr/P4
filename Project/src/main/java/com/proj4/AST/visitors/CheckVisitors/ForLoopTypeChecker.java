package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ForLoop;
import com.proj4.AST.visitors.TypeCheckVisitor;

public class ForLoopTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        ForLoop forLoop = (ForLoop) node;
        forLoop.inheritScope();
    }
}
