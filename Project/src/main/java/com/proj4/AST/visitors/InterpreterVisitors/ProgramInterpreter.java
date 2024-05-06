package com.proj4.AST.visitors.InterpreterVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.symbolTable.Scope;

public class ProgramInterpreter extends InterpreterVisitor {

    public void visit(AST node) {
        Scope.enter();
        node.visitChildren(new InterpreterDecider());
        Scope.exit();
    }
}