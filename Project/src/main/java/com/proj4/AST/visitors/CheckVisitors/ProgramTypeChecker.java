package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.symbolTable.Scope;

public class ProgramTypeChecker extends TypeCheckVisitor{

    public void visit(AST program){
        // Start of traversing the AST requires new scope
        Scope.enter();
        program.visitChildren(new CheckDecider());
        Scope.exit();
    }
}