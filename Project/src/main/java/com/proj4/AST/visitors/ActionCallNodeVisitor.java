package com.proj4.AST.visitors;

import com.proj4.AST.nodes.*;

public class ActionCallNodeVisitor implements NodeVisitor {

    public void visit(AST node) {
        ActionCall ActionCallNode = (ActionCall) node;
        System.out.println("\nVisiting Action DeclNode:");
        System.out.println(" ID = " + ActionCallNode.getIdentifier());
    }
}