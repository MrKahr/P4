package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.RuleDecl;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.UndefinedActionExpection;
import com.proj4.symbolTable.Scope;
import com.proj4.symbolTable.symbols.ActionSymbol;
import com.proj4.symbolTable.symbols.RuleSymbol;

public class RuleDeclTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        RuleDecl ruleDecl = (RuleDecl) node;
        Scope.inherit();
        //make sure the triggering actions are defined
        for (String identifier : ruleDecl.getTriggerActions()) {
            ActionSymbol action = Scope.getActionTable().get(identifier);
            if (action == null) {
                throw new UndefinedActionExpection("Undeclared action in rule declaration: Could not find \"" + identifier + "\"!");
            }
        }

        //make sure everything that happens in the rule is well typed
        ruleDecl.visitChild(new CheckDecider(), ruleDecl.getRuleBody());

        //bind the rule to the actions that trigger it
        for (String identifier : ruleDecl.getTriggerActions()) {
            Scope.declareRule(identifier, new RuleSymbol(ruleDecl.getRuleBody()));
        }

        Scope.exit();
    }
}
