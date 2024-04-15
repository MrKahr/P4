package com.proj4.AST.visitors;

import com.proj4.AST.nodes.*;

public class AssignmentNodeVisitor implements NodeVisitor {

    public void visit(AST node) {
        Assignment AssignmentNode = (Assignment) node;
        System.out.println("\nVisiting Action DeclNode:");
        System.out.println(" ID = " + AssignmentNode.getIdentifier());
    }
}