package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.*;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.UndefinedActionExpection;

public class StateDeclTypeChecker extends TypeCheckVisitor{

    public void visit(AST node){
        node.inheritScope();

        // Assumption: The state decl node is assumed to have child identifiers that it can check in the action table. 
        for (String actionIdentifier : ((StateDecl)node).getActionList()) { 
            
            // Actions need to be in table for declaration to be valid - assumption: states contain identifiers of actions
            if(!(node.getScope().getStateTable().contains(actionIdentifier))){
                throw new UndefinedActionExpection("Action " + actionIdentifier + " is not defined for state declaration" + ((StateDecl)node).getIdentifier());
            }
        }

        // If there is a body of action calls/statements, typecheck them!
        CheckDecider typeCheckDecider = new CheckDecider();

        for (AST ast : node.getChildren()) {
            typeCheckDecider.decideVisitor(ast);
        }

        // Add finished declaration to scope and synthesize it to parent
        node.getScope().declareState(((StateDecl)node).getIdentifier());
        node.synthesizeScope();
    }
}