package com.proj4.AST.nodes;

import java.util.ArrayList;

//technically a declaration, but shouldn't be usable in places where other declarations are
public class RuleDecl extends AST{
    //Field
    private ArrayList<String> actions;

    //Constructor
    public RuleDecl(If ruleBody){
        this.actions = new ArrayList<String>();
        addChild(ruleBody);
    }

    //Method
    public ArrayList<String> getTriggerActions(){
        return actions;
    }

    public If getRuleBody(){
        return (If) getChildren().get(0);
    }
}
