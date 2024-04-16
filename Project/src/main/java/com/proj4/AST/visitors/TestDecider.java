package com.proj4.AST.visitors;

import com.proj4.AST.nodes.*;
import com.proj4.AST.visitors.TestVisitors.*;

public class TestDecider implements VisitorDecider{
    //decide which visitor class to use for the given node
    public void decideVisitor(AST node){
        switch (node.getClass().getSimpleName()) {
            case "Program":
                node.acceptVisitor(new ProgramNodeVisitor());
                break;
            case "PrimDeclNode":
                node.acceptVisitor(new PrimNodeVisitor());
                break;
            case "ActionDeclNode":
                node.acceptVisitor(new ActionDeclNodeVisitor());
                break;
            default:
                System.err.println("UNRECOGNIZED NODE TYPE!\nGOT " + node.getClass().getSimpleName());
                break;
        }
    }
}
