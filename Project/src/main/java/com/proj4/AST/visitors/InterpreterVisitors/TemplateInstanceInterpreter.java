package com.proj4.AST.visitors.InterpreterVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.TemplateInstance;
import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.symbolTable.symbols.SymbolTableEntry;
import com.proj4.symbolTable.symbols.TemplateSymbol;

public class TemplateInstanceInterpreter implements NodeVisitor {

    public void visit(AST node){
        TemplateInstance templateInstance = (TemplateInstance) node;

        String templateType = templateInstance.getType();
        
        TemplateSymbol template = (TemplateSymbol)SymbolTableEntry.instantiateDefault(templateType, "Template", 0);

        //we make assignments to the fields in the order the fields are declared
        if (templateInstance.getChildren().size() > 0) {
            for (int index = 0; index < templateInstance.getChildren().size(); index++) {
                templateInstance.visitChild(new InterpreterDecider(), index);
                template.getContent().set(index, InterpreterVisitor.getInstance().getReturnSymbol());
            }
        }

        InterpreterVisitor.getInstance().setReturnSymbol(template);
    }
}