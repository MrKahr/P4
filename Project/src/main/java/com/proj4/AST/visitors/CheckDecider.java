package com.proj4.AST.visitors;

import com.proj4.AST.nodes.*;
import com.proj4.AST.visitors.CheckVisitors.*;

public class CheckDecider implements VisitorDecider {
     //decide which visitor class to use for the given node
     public void decideVisitor(AST node){
        switch (node.getClass().getSimpleName()) {
            case "Program":
                node.acceptVisitor(new ProgramTypeChecker());
                break;
            case "PrimitiveDecl":
                node.acceptVisitor(new PrimitiveDeclTypeChecker());
                break;
            case "a":
                node.acceptVisitor(null);
                break;
            default:
                System.err.println("UNRECOGNIZED NODE TYPE!\nGOT " + node.getClass().getSimpleName());
                break;
        }
    }
}
