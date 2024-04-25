package com.proj4.AST.nodes;

import java.util.ArrayList;

import com.proj4.exceptions.MalformedAstException;

//technically a declaration, but shouldn't be usable in places where other declarations are
public class RuleDecl extends AST{
    //Field
    private ArrayList<String> actions;

    //Constructor
    public RuleDecl(IfElse ruleBody){
        this.actions = new ArrayList<String>();
        addChild(ruleBody);
    }

    //Method
    public ArrayList<String> getTriggerActions(){
        return actions;
    }

    public IfElse getRuleBody(){
        try {
            return (IfElse) getChildren().get(0);
        } catch (ClassCastException cce) {
            throw new MalformedAstException("Could not find IfElse-node as child of RuleDecl! Found node of type \"" + getChildren().get(0).getClass().getSimpleName() + "\".");
        }
    }
}
