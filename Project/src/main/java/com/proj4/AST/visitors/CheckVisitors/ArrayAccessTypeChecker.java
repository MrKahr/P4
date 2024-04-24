package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ArrayAccess;
import com.proj4.AST.visitors.TypeCheckVisitor;

public class ArrayAccessTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        ArrayAccess arrayAccess = (ArrayAccess) node;
        arrayAccess.inheritScope();
    }
}
