package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.TemplateInstance;
import com.proj4.AST.visitors.TypeCheckVisitor;

public class TemplateInstanceTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        TemplateInstance templateInstance = (TemplateInstance) node;
        templateInstance.inheritScope();
    }
}
