package com.proj4.AST;

import java.util.ArrayList;

import com.proj4.symbolTable.Scope;

public class AbstractSyntaxTree {

    //Field
    private SyntacticCategory category;
    private AbstractSyntaxTree parent;
    private ArrayList<AbstractSyntaxTree> children;
    private Scope scope;
    private String value;

    //Constructor
    AbstractSyntaxTree(SyntacticCategory category, AbstractSyntaxTree parent, ArrayList<AbstractSyntaxTree> children, String value){
        this.category = category;
        this.parent = parent;
        this.children = children;
        this.scope = new Scope();
        this.value = value;
    }

    //used for creating the root of the tree. Everything other than scope can be null
    AbstractSyntaxTree(SyntacticCategory category){
        this.category = category;
        this.scope = new Scope();   //we must always have a scope
    }

    //Method
    public SyntacticCategory getCategory() {
        return category;
    }

    public ArrayList<AbstractSyntaxTree> getChildren() {
        return children;
    }

    public AbstractSyntaxTree getParent() {
        return parent;
    }

    public Scope getScope() {
        return scope;
    }

    public String getValue() {
        return value;
    }

    public void setChildren(ArrayList<AbstractSyntaxTree> children) {
        this.children = children;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }
    
    public void setValue(String value) {
        this.value = value;
    }

    //put a new child on the list of children
    public void addChild(AbstractSyntaxTree newChild){
        children.add(newChild);
    }

    public static void walk(AbstractSyntaxTree AST){
        AST.getChildren().forEach((child) -> {AbstractSyntaxTree.walk(child);});
    }
}