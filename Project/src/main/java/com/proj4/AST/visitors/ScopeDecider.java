package com.proj4.AST.visitors;

import com.proj4.AST.nodes.*;
import com.proj4.AST.visitors.ScopeVisitors.*;

public class ScopeDecider implements VisitorDecider {
     //decide which visitor class to use for the given node
     public void decideVisitor(AST node){
        switch (node.getClass().getSimpleName()) {
            case "Program":
                node.acceptVisitor(null);
                break;
            case "PrimDeclNode":
                node.acceptVisitor(null);
                break;
            case "ActionDeclNode":
                node.acceptVisitor(null);
                break;
            default:
                System.err.println("UNRECOGNIZED NODE TYPE!\nGOT " + node.getClass().getSimpleName());
                break;
        }
    }
}
