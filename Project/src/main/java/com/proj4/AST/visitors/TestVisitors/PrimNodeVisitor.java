package com.proj4.AST.visitors.TestVisitors;

import com.proj4.AST.nodes.*;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.AST.visitors.TestDecider;

public class PrimNodeVisitor implements NodeVisitor {

    public void visit(AST node) {
        PrimitiveDecl declNode = (PrimitiveDecl) node;
        System.out.println("\nVisiting DeclNode:");
        node.getChildren().get(0).acceptDecider(new TestDecider());
    }
}
