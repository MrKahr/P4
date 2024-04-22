package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.StateDecl;
import com.proj4.AST.visitors.TypeCheckVisitor;

public class StateDeclTypeChecker extends TypeCheckVisitor{

    public void visit(AST node){
        StateDecl stateDecl = (StateDecl) node;
        stateDecl.inheritScope();
    }
}