package com.proj4.AST.visitors.InterpreterVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.symbolTable.ScopeManager;

public class BodyInterpreter implements NodeVisitor {

    public void visit(AST node) {
        ScopeManager.getInstance().inherit();
        node.visitChildren(new InterpreterDecider());
        ScopeManager.getInstance().synthesize();
    }
}