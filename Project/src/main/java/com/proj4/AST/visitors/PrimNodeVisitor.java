package com.proj4.AST.visitors;

import com.proj4.AST.nodes.*;

public class PrimNodeVisitor implements NodeVisitor {

    public void visit(AST node) {
        PrimDeclNode declNode = (PrimDeclNode) node;
        System.out.println("\nVisiting DeclNode:");
        System.out.println("Type = " + declNode.getType() + " ID = " + declNode.getID());
    }
}
