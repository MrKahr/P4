package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ForLoop;
import com.proj4.AST.nodes.Identifiable;
import com.proj4.AST.nodes.Statement;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.MalformedAstException;
import com.proj4.exceptions.MismatchedTypeException;

public class ForLoopTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        ForLoop forLoop = (ForLoop) node;
        forLoop.inheritScope();

        try {
            Identifiable iterator = (Identifiable) forLoop.getIterator();       //the iterator should have an identifier
            Statement iteratorAction = (Statement) forLoop.getIteratorAction(); //what we do with the iterator after the loop should be a statement of some kind 
        } catch (ClassCastException cce) {
            throw new MalformedAstException("Recived malformed iteration statement in for loop. Check iterator and iterator action");
        }

        forLoop.visitChild(new CheckDecider(), forLoop.getCondition());
        if (!TypeCheckVisitor.getFoundType().equals("Boolean")) {
            throw new MismatchedTypeException("For loop condition does not return a boolean! Found \"" + TypeCheckVisitor.getFoundType() + "\".");
        }
        if (!TypeCheckVisitor.getFoundComplexType().equals("Primitive")) {
            throw new MismatchedTypeException("For loop condition does not return a primitive! Found \"" + TypeCheckVisitor.getFoundComplexType() + "\".");
        }
        
        //Make sure everything in the loop is well typed
        forLoop.visitChildren(new CheckDecider());
    }
}
