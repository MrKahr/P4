package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Return;
import com.proj4.AST.visitors.TypeCheckVisitor;

public class ReturnTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        Return returnNode = (Return) node;
        returnNode.inheritScope();
    }
}
