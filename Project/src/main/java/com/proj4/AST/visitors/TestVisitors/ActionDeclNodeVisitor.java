package com.proj4.AST.visitors.TestVisitors;

import com.proj4.AST.nodes.*;
import com.proj4.AST.visitors.NodeVisitor;

public class ActionDeclNodeVisitor implements NodeVisitor {

    public void visit(AST node) {
        ActionDeclNode declNode = (ActionDeclNode) node;
        System.out.println("\nVisiting Action DeclNode:");
        System.out.println("Type = " + declNode.getType() + " ID = " + declNode.getID());
    }
}
