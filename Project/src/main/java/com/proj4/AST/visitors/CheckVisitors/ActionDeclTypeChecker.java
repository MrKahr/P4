package com.proj4.AST.visitors.CheckVisitors;

import java.util.ArrayList;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ActionDecl;
import com.proj4.AST.nodes.Declaration;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.MalformedAstException;
import com.proj4.exceptions.VariableAlreadyDefinedException;
import com.proj4.symbolTable.GlobalScope;
import com.proj4.symbolTable.ScopeManager;
import com.proj4.symbolTable.symbols.ActionSymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;
import com.proj4.symbolTable.symbols.TemplateSymbol;

public class ActionDeclTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        ActionDecl actionDecl = (ActionDecl) node;
        ScopeManager.getInstance().inherit();

        // Check whether action is part of the inbuilt actions
        if(GlobalScope.getInstance().getInbuiltActions().contains(actionDecl.getIdentifier())){
            throw new VariableAlreadyDefinedException("Cannot redeclare inbuilt action \"" + actionDecl.getIdentifier());
        }
        
        //Check whether action is already defined in scope
        if(GlobalScope.getInstance().getActionTable().containsKey(actionDecl.getIdentifier())){
            throw new VariableAlreadyDefinedException("Action \"" + actionDecl.getIdentifier() + "\" is already defined!");
        }
        //Check whether a template is using this action's name (assuming that anything that exists in the blueprint table also exists as a map and so on)
        if (GlobalScope.getInstance().getBlueprintTable().containsKey(actionDecl.getIdentifier())) {
            throw new VariableAlreadyDefinedException("Template is using reserved identifier \"" + actionDecl.getIdentifier() + "\"!");
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
        action.setInitialScope(ScopeManager.getInstance().getCurrent());

        //type check the body itself, which is not in the children list
        actionDecl.visitChild(new CheckDecider(), actionDecl.getBody());

        //clear current action
        TypeCheckVisitor.setCurrentAction(null);
    
        //Add declared action to action table 
        GlobalScope.getInstance().getActionTable().put(actionDecl.getIdentifier(), action);

        //In this language, given some action "a", we can write a.RESULT to get the most recently returned value
        //So let's make a template blueprint for this action (we'll instantiate the actual template with the interpreter)
        TemplateSymbol blueprint = new TemplateSymbol();
        blueprint.addContent(
            SymbolTableEntry.instantiateDefault(
                actionDecl.getType(),
                actionDecl.getComplexReturnType(),
                actionDecl.getNestingLevel()
            )
        );
        //To navigate a template, we need a map - NOTE not java map
        ArrayList<String> map = new ArrayList<>();
        map.add("RESULT");  //the one field we have is accessed with .RESULT
        
        //Now let's add them to the appropriate tables
        GlobalScope.getInstance().getTemplateMapTable().put(actionDecl.getIdentifier(), map);
        GlobalScope.getInstance().getBlueprintTable().put(actionDecl.getIdentifier(), blueprint);

        ScopeManager.getInstance().exit();
    }
}