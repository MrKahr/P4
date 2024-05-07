package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ActionDecl;
import com.proj4.AST.nodes.Declaration;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.MalformedAstException;
import com.proj4.exceptions.VariableAlreadyDefinedException;
import com.proj4.symbolTable.Scope;
import com.proj4.symbolTable.symbols.ActionSymbol;

public class ActionDeclTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        ActionDecl actionDecl = (ActionDecl) node;
        Scope.inherit();
        // Check whether action is already defined in scope
        if(Scope.getActionTable().containsKey(actionDecl.getIdentifier())){
            throw new VariableAlreadyDefinedException("Action \"" + actionDecl.getIdentifier() + "\" is already defined!");
        }

        //Construct symbol to represent the action
        ActionSymbol action = new ActionSymbol(
            actionDecl.getType(), 
            actionDecl.getComplexReturnType(), 
            actionDecl.getBody(),
            actionDecl.getNestingLevel()
        );

        //set the action body before checking children
        TypeCheckVisitor.setCurrentAction(actionDecl.getIdentifier());

        //make sure all child declarations (parameters) are well-typed and in this scope, and add their names to the parameter list
        try {
            for (AST child : actionDecl.getChildren()) {
            actionDecl.visitChild(new CheckDecider(), (Declaration) child);
            //remember the name of the parameter
            action.addParameterName(((Declaration) child).getIdentifier());
            }
        } catch (ClassCastException cce) {
            throw new MalformedAstException("Expected only declarations in children list of actionDecl!");
        }
        
        //save the scope in the action symbol
        action.setInitialScope(Scope.getCurrent());

        //type check the body itself, which is not in the children list
        actionDecl.visitChild(new CheckDecider(), actionDecl.getBody());

        //clear current action
        TypeCheckVisitor.setCurrentAction(null);
    
        //Add declared action to action table 
        Scope.getActionTable().put(actionDecl.getIdentifier(), action);

        Scope.exit();
    }
}