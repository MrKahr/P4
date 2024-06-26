package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ForLoop;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.MalformedAstException;
import com.proj4.exceptions.MismatchedTypeException;
import com.proj4.symbolTable.ScopeManager;

public class ForLoopTypeChecker implements NodeVisitor{
    
    public void visit(AST node){
        ForLoop forLoop = (ForLoop) node;
        ScopeManager.getInstance().inherit();

        try {
            forLoop.visitChild(new CheckDecider(), (AST)forLoop.getIterator());
        } catch (ClassCastException cce) {
            throw new MalformedAstException("Expected AST node in forLoop iterator! Got a ClassCastException!");
        }

        forLoop.visitChild(new CheckDecider(), forLoop.getCondition());
        if (!TypeCheckVisitor.getInstance().getFoundType().equals("Boolean")) {
            throw new MismatchedTypeException("For loop condition does not return a boolean! Found \"" + TypeCheckVisitor.getInstance().getFoundType() + "\".");
        }
        if (!TypeCheckVisitor.getInstance().getFoundComplexType().equals("Primitive")) {
            throw new MismatchedTypeException("For loop condition does not return a primitive! Found \"" + TypeCheckVisitor.getInstance().getFoundComplexType() + "\".");
        }
        
        //Make sure everything in the loop is well typed
        forLoop.visitChildren(new CheckDecider());

        //check the iteratoraction afterwards so it can use things declared in the loop
        forLoop.visitChild(new CheckDecider(), forLoop.getIteratorAction());

        ScopeManager.getInstance().synthesize();

    }
}
