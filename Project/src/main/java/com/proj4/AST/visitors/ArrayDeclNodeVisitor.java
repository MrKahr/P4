package com.proj4.AST.visitors;

import com.proj4.AST.nodes.*;

public class ArrayDeclNodeVisitor implements NodeVisitor {

    public void visit(AST node) {
        ArrayDecl ArrayDeclNode = (ArrayDecl) node;
        System.out.println("\nVisiting Action DeclNode:");
        System.out.println(" ID = " + ArrayDeclNode.getIdentifier());
    }
}