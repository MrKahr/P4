package com.proj4.AST.visitors.TestVisitors;

import com.proj4.AST.nodes.*;
import com.proj4.AST.visitors.NodeVisitor;

public class ProgramNodeVisitor implements NodeVisitor {

    public void visit(AST program){
        System.out.println("Whats up im lookin at da program im de programnodevisitor check me out");
        program.visitChildren();
    }
}