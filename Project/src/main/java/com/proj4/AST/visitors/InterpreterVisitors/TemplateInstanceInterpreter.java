package com.proj4.AST.visitors.InterpreterVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.TemplateInstance;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

public class TemplateInstanceInterpreter extends InterpreterVisitor {

    public void visit(AST node){
        TemplateInstance templateInstance = (TemplateInstance) node;

        String templateType = templateInstance.getType();
        
        InterpreterVisitor.setReturnSymbol(SymbolTableEntry.instantiateDefault(templateType, "Template", 0));
    }
}