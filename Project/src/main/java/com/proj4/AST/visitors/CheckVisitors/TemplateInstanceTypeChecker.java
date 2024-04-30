package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.TemplateInstance;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

public class TemplateInstanceTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        TemplateInstance templateInstance = (TemplateInstance) node;
        templateInstance.inheritScope();

        String templateType = templateInstance.getType();

        SymbolTableEntry.instantiateDefault(templateType, "Template", 0);

        TypeCheckVisitor.setFoundType(templateType, "Template", 0);
    }
}
