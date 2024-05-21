package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.StateDecl;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.exceptions.*;
import com.proj4.symbolTable.GlobalScope;
import com.proj4.symbolTable.ScopeManager;
import com.proj4.symbolTable.symbols.StateSymbol;

public class StateDeclTypeChecker implements NodeVisitor{


    public void visit(AST node){
        StateDecl stateDecl = (StateDecl) node;
        ScopeManager.getInstance().inherit();
        // Check whether state decl is already defined in scope
        if(GlobalScope.getInstance().getStateTable().keySet().contains(stateDecl.getIdentifier())){
            throw new StateAlreadyDefinedExpection("State " + stateDecl.getIdentifier() + " is already defined in state table");
        }
       

        // Assumption: The state decl node is assumed to have child identifiers that it can check in the action table. 
        for (String actionIdentifier : stateDecl.getActionList()) { 
            
            // Actions need to be in table for declaration to be valid - assumption: states contain identifiers of actions
            if(!(GlobalScope.getInstance().getActionTable().containsKey(actionIdentifier))){
                throw new UndefinedActionExpection("Action " + actionIdentifier + " is not defined for state declaration" + ((StateDecl)node).getIdentifier());
            }
        }

        // Check the body of the state if it exists
        if (stateDecl.getBody() != null) {
            stateDecl.visitChild(new CheckDecider(), stateDecl.getBody());
        }

        // Create a StateSymbol and add it to the state table
        GlobalScope.getInstance().declareState(stateDecl.getIdentifier(), new StateSymbol(stateDecl.getBody(), stateDecl.getActionList()));
        ScopeManager.getInstance().exit();
    }
}