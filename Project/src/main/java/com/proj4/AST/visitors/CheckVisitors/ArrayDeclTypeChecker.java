package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ArrayDecl;
import com.proj4.AST.visitors.TypeCheckVisitor;

public class ArrayDeclTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        ArrayDecl arrayDecl = (ArrayDecl) node;
        arrayDecl.inheritScope();
    }
}
