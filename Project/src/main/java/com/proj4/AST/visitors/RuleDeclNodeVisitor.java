package com.proj4.AST.visitors;

import com.proj4.AST.nodes.*;

public class RuleDeclNodeVisitor {

    public void visit(AST node) {
        RuleDecl ruleDecl = (RuleDecl) node;
        System.out.println("\nVisiting RuleDeclNode:");
        System.out.println("Contains " + ruleDecl.getTriggerActions() + " " + ruleDecl.getRuleBody());
    }
    
}

