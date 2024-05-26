package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.RuleDecl;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.exceptions.UndefinedActionExpection;
import com.proj4.symbolTable.GlobalScope;
import com.proj4.symbolTable.ScopeManager;
import com.proj4.symbolTable.symbols.ActionSymbol;
import com.proj4.symbolTable.symbols.RuleSymbol;

public class RuleDeclTypeChecker implements NodeVisitor{

    public void visit(AST node){
        RuleDecl ruleDecl = (RuleDecl) node;
        ScopeManager.getInstance().inherit();
        //make sure the triggering actions are defined
        for (String identifier : ruleDecl.getTriggerActions()) {
            // Check Action in global scope first
            if(!GlobalScope.getInstance().getInbuiltActions().contains(identifier)) {
                ActionSymbol action = GlobalScope.getInstance().getActionTable().get(identifier);
                if (action == null) {
                    throw new UndefinedActionExpection("Undeclared action in rule declaration: Could not find \"" + identifier + "\"!");
                }
            }
        }

        //make sure everything that happens in the rule is well typed
        ruleDecl.visitChild(new CheckDecider(), ruleDecl.getRuleBody());

        //bind the rule to the actions that trigger it
        for (String identifier : ruleDecl.getTriggerActions()) {
            RuleSymbol ruleSymbol = new RuleSymbol(ruleDecl.getRuleBody());
            ruleSymbol.setInitialScope(ScopeManager.getInstance().getCurrent());
            GlobalScope.getInstance().declareRule(identifier, ruleSymbol);
        }
        ScopeManager.getInstance().exit();
    }
}
