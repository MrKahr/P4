package com.proj4.AST.visitors;

import com.proj4.AST.nodes.*;

public class StmtListNodeVisitor {
    public void visit(AST node) {
        StmtList listNode = (StmtList) node;
        System.out.println("\nVisiting StmtListNode:");
    }
}
