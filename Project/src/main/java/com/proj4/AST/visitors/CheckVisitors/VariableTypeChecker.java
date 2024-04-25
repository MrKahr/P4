package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Variable;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.UndefinedVariableException;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

public class VariableTypeChecker extends TypeCheckVisitor{

    public void visit(AST node){
        Variable variable = (Variable) node;
        variable.inheritScope();
        SymbolTableEntry entry = variable.getScope().getVTable().get(variable.getIdentifier());
        if (entry == null) {
            throw new UndefinedVariableException("Variable \"" + variable.getIdentifier() + "\" not defined in current scope!");
        } else {
            setFoundType(entry.getType(), entry.getComplexType());   
        }
    }
    //not visiting children here because Variable-nodes are leaf nodes
}