package com.proj4.AST.visitors.InterpreterVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.symbolTable.ScopeManager;

public class BodyInterpreter extends InterpreterVisitor {

    public void visit(AST node) {
        ScopeManager.getInstance().inherit();
        node.visitChildren(new InterpreterDecider());
        ScopeManager.getInstance().synthesize();
    }
}