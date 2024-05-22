package com.proj4.AST.visitors.InterpreterVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Variable;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.symbolTable.ScopeManager;

public class VariableInterpreter implements NodeVisitor {
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
        InterpreterVisitor.getInstance().setReturnSymbol(ScopeManager.getInstance().getCurrent().getVariableTable().get(variable.getIdentifier()));
    }
}
