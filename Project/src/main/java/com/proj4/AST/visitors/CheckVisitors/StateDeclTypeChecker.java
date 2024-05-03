package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.StateDecl;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.*;
import com.proj4.symbolTable.Scope;

public class StateDeclTypeChecker extends TypeCheckVisitor{


    public void visit(AST node){
        StateDecl stateDecl = (StateDecl) node;

        // Check whether state decl is already defined in scope
        if(Scope.getStateTable().contains(stateDecl.getIdentifier())){
            throw new StateAlreadyDefinedExpection("State " + stateDecl.getIdentifier() + " is already defined in state table");
        }
       

        // Assumption: The state decl node is assumed to have child identifiers that it can check in the action table. 
        for (String actionIdentifier : stateDecl.getActionList()) { 
            
            // Actions need to be in table for declaration to be valid - assumption: states contain identifiers of actions
            if(!(Scope.getATable().containsKey(actionIdentifier))){
                throw new UndefinedActionExpection("Action " + actionIdentifier + " is not defined for state declaration" + ((StateDecl)node).getIdentifier());
            }
        }

        // Add finished declaration to scope and synthesize it to parent
        stateDecl.getScope().declareState(stateDecl.getIdentifier());
    }
}