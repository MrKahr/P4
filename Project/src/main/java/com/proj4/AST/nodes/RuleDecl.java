package com.proj4.AST.nodes;

//technically a declaration, but shouldn't be usable in places where other declarations are
public class RuleDecl extends AST{
    //Field
    private String[] actions;

    //Constructor
    public RuleDecl(String[] actions, IfElse ruleBody){
        this.actions = actions;
        addChild(ruleBody);
    }

    //Method
    public String[] getTriggerActions(){
        return actions;
    }

    public IfElse getRuleBody(){
        return (IfElse) getChildren().get(0);
    }
}
