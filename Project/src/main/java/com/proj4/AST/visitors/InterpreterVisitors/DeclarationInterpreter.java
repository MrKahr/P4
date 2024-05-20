package com.proj4.AST.visitors.InterpreterVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Declaration;
import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.symbolTable.ScopeManager;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

public class DeclarationInterpreter extends InterpreterVisitor {

    public void visit(AST node) {
        Declaration declaration = (Declaration) node;
        ScopeManager.getInstance().getCurrent().getVariableTable().put(
            declaration.getIdentifier(), 
            SymbolTableEntry.instantiateDefault(
                declaration.getType(), 
                declaration.getComplexType(), 
                declaration.getNestingLevel())
        );
        if (declaration.getChildren().size() > 0) {
            declaration.visitChild(new InterpreterDecider(), declaration.getInitialAssignment());
        }  
    }
}
