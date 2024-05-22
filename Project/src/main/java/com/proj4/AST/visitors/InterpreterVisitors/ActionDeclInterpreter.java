package com.proj4.AST.visitors.InterpreterVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ActionDecl;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.symbolTable.ScopeManager;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

public class ActionDeclInterpreter implements NodeVisitor {
    private Boolean verbose = false;

    public ActionDeclInterpreter(){}
    public ActionDeclInterpreter(Boolean verbose){
        this.verbose = verbose;
    }

    public void visit(AST node) {
        //Nothing to do here. The type checker creates all the bindings we need, and action-templates are created before the rest of the program gets interpreted
    }
}