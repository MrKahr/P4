package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.TemplateInstance;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.VariableAlreadyDefinedException;
import com.proj4.symbolTable.Scope;
import com.proj4.symbolTable.symbols.ComplexSymbol;

public class TemplateInstanceTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        TemplateInstance templateInstance = (TemplateInstance) node;
        templateInstance.inheritScope();

        String templateType = templateInstance.getInstanceType();
        String identifier = templateInstance.getIdentifier();
        ComplexSymbol blueprint = Scope.getBTable().get(templateType);  //get the ComplexTemplate bound to the given identifier
        Scope scope = templateInstance.getScope();
        if (scope.getDTable().contains(identifier)) {
            throw new VariableAlreadyDefinedException("The variable \"" + "\" is already defined!" );
        } else {
            scope.getVTable().put(identifier, new ComplexSymbol(blueprint));
        }

        templateInstance.synthesizeVariable(identifier);
    }
}
