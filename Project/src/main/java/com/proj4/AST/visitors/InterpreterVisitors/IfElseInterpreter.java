package com.proj4.AST.visitors.InterpreterVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.IfElse;
import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.symbolTable.symbols.BooleanSymbol;

public class IfElseInterpreter extends InterpreterVisitor {
    private Boolean verbose = false;

    public IfElseInterpreter(){}
    public IfElseInterpreter(Boolean verbose){
        this.verbose = verbose;
    }

    public void visit(AST node) {
        IfElse ifElse = (IfElse) node;
        ifElse.visitChild(new InterpreterDecider(), ifElse.getCondition());
        Boolean truthValue = ((BooleanSymbol)InterpreterVisitor.getReturnSymbol()).getValue();

        if(this.verbose){
            System.out.println(this.getClass().getSimpleName() + ": If-statement condition is " + truthValue + ".");
        }
        if (truthValue) {
            ifElse.visitChild(new InterpreterDecider(), ifElse.getThenBlock());
        } else if (ifElse.getElseBlock() != null){
            ifElse.visitChild(new InterpreterDecider(), ifElse.getElseBlock());
        }
    }
}