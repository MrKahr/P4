package com.proj4.AST.visitors.InterpreterVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ForLoop;
import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.symbolTable.ScopeManager;
import com.proj4.symbolTable.symbols.BooleanSymbol;

public class ForLoopInterpreter extends InterpreterVisitor {

    public void visit(AST node) {
        ForLoop forLoop = (ForLoop) node;
        ScopeManager.getInstance().inherit();
        //interpret the iterator
        forLoop.visitChild(new InterpreterDecider(), (AST)forLoop.getIterator());
        //interpret the condition
        forLoop.visitChild(new InterpreterDecider(), forLoop.getCondition());
        //interpret the body if the condition is true
        //interpret the iteratorAction if the condition is true
        if (((BooleanSymbol)InterpreterVisitor.getReturnSymbol()).getValue()) {
            forLoop.visitChild(new InterpreterDecider(), forLoop.getBody());
            forLoop.visitChild(new InterpreterDecider(), forLoop.getIteratorAction());
        }
        ScopeManager.getInstance().synthesize();
    }
}