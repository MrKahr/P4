package com.proj4.AST.visitors.InterpreterVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.visitors.InterpreterVisitor;

public class ActionDeclInterpreter extends InterpreterVisitor {

    public void visit(AST node) {
        //The interpreter handles all the bindings, so there's nothing to interpret here
    }
}