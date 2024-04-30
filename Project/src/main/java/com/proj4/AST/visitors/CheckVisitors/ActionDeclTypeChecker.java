package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ActionDecl;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.VariableAlreadyDefinedException;
import com.proj4.symbolTable.Scope;
import com.proj4.symbolTable.symbols.ActionSymbol;

public class ActionDeclTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        ActionDecl actionDecl = (ActionDecl) node;
        actionDecl.inheritScope();
        if(Scope.getATable().containsKey(actionDecl.getIdentifier())){
            throw new VariableAlreadyDefinedException("Action \"" + actionDecl.getIdentifier() + "\" is already defined!");
        }

        // Add declared function to action table 
            Scope.getATable().put(
                actionDecl.getIdentifier(), 
                new ActionSymbol(
                    actionDecl.getType(), 
                    actionDecl.getComplexReturnType(), 
                    actionDecl.getBody(),
                    actionDecl.getNestingLevel()
                    )
                );
        //set the action body before checking children
        TypeCheckVisitor.setCurrentAction(actionDecl.getIdentifier());

        // Make sure all child declarations are well-typed and in this scope
        actionDecl.visitChildren(new CheckDecider());

        //clear current action
        TypeCheckVisitor.setCurrentAction(null);

        // Check whether return is defined from possible types (check whether primitive or template type)
        // Check whether action is already defined in scope 
    
  
       
    }
}