package com.proj4.AST.visitors.InterpreterVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Variable;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.symbolTable.GlobalScope;
import com.proj4.symbolTable.ScopeManager;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

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
        SymbolTableEntry entry = ScopeManager.getInstance().getCurrent().getVariableTable().get(variable.getIdentifier());
        if (entry == null) {
            //if the variable is not in the variable table, we might be looking for an action's corresponding template
            entry = GlobalScope.getInstance().getResultTable().get(variable.getIdentifier());
        }

        InterpreterVisitor.getInstance().setReturnSymbol(entry);
    }
}
