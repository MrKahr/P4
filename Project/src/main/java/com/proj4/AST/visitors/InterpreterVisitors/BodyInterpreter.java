package com.proj4.AST.visitors.InterpreterVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.AST.visitors.InterpreterVisitor;

public class BodyInterpreter extends InterpreterVisitor {

    public void visit(AST node) {
        node.visitChildren(new InterpreterDecider());
    }
}