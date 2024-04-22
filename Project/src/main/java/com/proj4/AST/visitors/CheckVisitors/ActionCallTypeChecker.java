package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ActionCall;
import com.proj4.AST.visitors.TypeCheckVisitor;

public class ActionCallTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        ActionCall actionCall = (ActionCall) node;
        actionCall.inheritScope();
    }
}