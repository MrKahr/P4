package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ActionDecl;
import com.proj4.AST.visitors.TypeCheckVisitor;

public class ActionDeclTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        ActionDecl actionDecl = (ActionDecl) node;
        actionDecl.inheritScope();
    }
}