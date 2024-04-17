package com.proj4.AST.visitors.TestVisitors;

import com.proj4.AST.nodes.*;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.AST.visitors.TestDecider;

public class PrimNodeVisitor implements NodeVisitor {

    public void visit(AST node) {
        PrimDeclNode declNode = (PrimDeclNode) node;
        System.out.println("\nVisiting DeclNode:");
        System.out.println("Type = " + declNode.getType() + " ID = " + declNode.getID());
        node.getChildren().get(0).acceptDecider(new TestDecider());
    }
}
