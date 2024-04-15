package com.proj4.AST.visitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.BoolExp;

public class BoolExpNodeVisitor implements NodeVisitor {
    
    @Override
    public void visit(AST node) {
        BoolExp castNode = (BoolExp) node;

        System.out.println("Visiting: " + castNode.getClass().getSimpleName());

    }



}
