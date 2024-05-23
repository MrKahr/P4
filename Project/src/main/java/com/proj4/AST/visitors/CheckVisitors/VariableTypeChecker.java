package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Variable;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.UndefinedVariableException;
import com.proj4.symbolTable.GlobalScope;
import com.proj4.symbolTable.symbols.ArraySymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

public class VariableTypeChecker implements NodeVisitor{

    public void visit(AST node){
        Variable variable = (Variable) node;
        SymbolTableEntry entry = variable.getScope().getVariableTable().get(variable.getIdentifier());
        if (entry == null) {
            //if the variable is not in the variable table, we might be looking for an action's corresponding template
            entry = GlobalScope.getInstance().getResultTable().get(variable.getIdentifier());
        }

        if (entry == null) {
            throw new UndefinedVariableException("Variable \"" + variable.getIdentifier() + "\" not defined in current scope!");
        } else {
            if (entry.getComplexType().equals("Array")) {
                ArraySymbol arrayEntry = (ArraySymbol) entry;   //Branching to ensure nesting level gets set correctly in typchecker
                TypeCheckVisitor.getInstance().setFoundType(arrayEntry.getType(), arrayEntry.getComplexType(), arrayEntry.getNestingLevel());
            } else {
                TypeCheckVisitor.getInstance().setFoundType(entry.getType(), entry.getComplexType(), -1);
            }
        }
    }
    //not visiting children here because Variable-nodes are leaf nodes
}