package com.proj4.AST.visitors.InterpreterVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Variable;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.symbolTable.Scope;

public class VariableInterpreter extends InterpreterVisitor {

    public void visit(AST node) { 
        Variable variable = (Variable) node;
        InterpreterVisitor.setReturnSymbol(Scope.getCurrent().getVariableTable().get(variable.getIdentifier()));
        System.out.println("Fetching symbol bound to identifier \"" + variable.getIdentifier() + "\".");
    }
}
