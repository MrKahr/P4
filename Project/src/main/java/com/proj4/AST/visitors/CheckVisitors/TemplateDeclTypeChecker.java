package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.TemplateDecl;
import com.proj4.AST.visitors.TypeCheckVisitor;

public class TemplateDeclTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        TemplateDecl templateDecl = (TemplateDecl) node;
        templateDecl.inheritScope();
        //note: template must not have identifier that is equal to "Integer", "Boolean", or "String" since those are reserved primitive types

        //how to typecheck:

        //typecheck a child
        //add child as field in blueprint
        //repeat for all children

        //if child is another template
        //create an instance of that template as a field
    }
}
