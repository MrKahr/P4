package com.proj4.AST.visitors.InterpreterVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.visitors.NodeVisitor;

public class RuleDeclInterpreter implements NodeVisitor {

    public void visit(AST node) {
        //there's nothing to interpret in a rule declaration. The type checker handles the bindings
        //TODO: consider if the bindings should be handled in the interpreter
    }
}