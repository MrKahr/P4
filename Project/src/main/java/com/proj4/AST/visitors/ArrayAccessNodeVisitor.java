package com.proj4.AST.visitors;

import com.proj4.AST.nodes.*;

public class ArrayAccessNodeVisitor implements NodeVisitor {

    public void visit(AST node) {
        ArrayAccess ArrayAccessNode = (ArrayAccess) node;
        System.out.println("\nVisiting Action DeclNode:");
        System.out.println(" ID = " + ArrayAccessNode.getIdentifier());
    }
}