package com.proj4.AST.visitors.InterpreterVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Variable;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.symbolTable.ScopeManager;

public class VariableInterpreter extends InterpreterVisitor {
    private Boolean verbose = false;

    public VariableInterpreter(){}
    public VariableInterpreter(Boolean verbose){
        this.verbose = verbose;
    }

    public void visit(AST node) {
        Variable variable = (Variable) node;
          if(this.verbose) {
            System.out.println(this.getClass().getSimpleName() + ": Fetching symbol bound to identifier \"" + variable.getIdentifier() + "\".");
        }
        InterpreterVisitor.setReturnSymbol(ScopeManager.getInstance().getCurrent().getVariableTable().get(variable.getIdentifier()));
    }
}
