package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.RuleDecl;
import com.proj4.AST.visitors.TypeCheckVisitor;

public class RuleDeclTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        RuleDecl ruleDecl = (RuleDecl) node;
        ruleDecl.inheritScope();
    }
}
