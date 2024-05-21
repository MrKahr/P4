package com.proj4.AST.visitors.InterpreterVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ActionDecl;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.symbolTable.ScopeManager;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

public class ActionDeclInterpreter extends InterpreterVisitor {
    private Boolean verbose = false;

    public ActionDeclInterpreter(){}
    public ActionDeclInterpreter(Boolean verbose){
        this.verbose = verbose;
    }

    public void visit(AST node) {
        ActionDecl actionDecl = (ActionDecl) node;

        if(this.verbose){
            System.out.println("Attempting to instantiate actionTemplate with identifier \"" + actionDecl.getIdentifier() + "\"");
        }
        //Create an instance of the action's corresponding template and bind it to the identifier so we can use .RESULT
        ScopeManager.getInstance().getCurrent().declareVariable(
            actionDecl.getIdentifier(),
            SymbolTableEntry.instantiateDefault(
                actionDecl.getIdentifier(),
                "Template",
                -1
            )
        );
    }
}