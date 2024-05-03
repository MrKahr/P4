package com.proj4.AST.nodes;

import java.util.ArrayList;
import java.util.List;

import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.AST.visitors.VisitorDecider;
import com.proj4.symbolTable.Scope;

public abstract class AST {
    //Field
    private ArrayList<AST> children = new ArrayList<AST>();
    private AST parent;

    //Method
    public ArrayList<AST> getChildren() {
        return children;
    }

    public AST getChild(Integer index) {
        return children.get(index);
    }

    public AST getParent(){
        return parent;
    }
    
    public Scope getScope(){
        return Scope.getScopeStack().peek();
    }

    //put a new child on the list of children
    public void addChild(AST newChild){
        children.add(newChild);
        // newChild.setParent(this);
    }

    // public void setParent(AST parent){
    //     this.parent = parent;
    // }
/*
    public void setScope(Scope scope){
        this.scope = scope;
    }
*/
    public void printTree(){
        Integer currentLevel = 0;
        List<List<AST>> result = new ArrayList<>();
        getLevel(this, currentLevel, result);
        
        for (List<AST> list : result) {
            // Avoid printing level for leaf nodes
            if(list.size() > 0){
                System.out.println("\nLevel " + currentLevel);
            }
            for (AST ast : list) {
                System.out.print(ast.getClass().getSimpleName() + " | ");
            }
            currentLevel++;
        }
 
    }

    public void getLevel(AST root, Integer level, List<List<AST>> result){
        if (root == null) {
            return;
        }

        if (result.size() >= level) {
            result.add(new ArrayList<>());
        }

        result.get(level).add(root);

        for (AST ast : root.getChildren()) {
            getLevel(ast, level + 1, result);
        }

    }

    //this method can be used to control which VisitorDecider that will pick a visitor for this node
    //to start a visiting sequence, simply call this method on an abstract syntax tree
    public void acceptDecider(VisitorDecider decider){
        decider.decideVisitor(this);
    }

    //this method activates the given NodeVisitor
    public void acceptVisitor(NodeVisitor visitor){
        visitor.visit(this);
    }

    //specify a decider and use it to visit all children
    public void visitChildren(VisitorDecider decider){
        children.forEach((child) -> {
            decider.decideVisitor(child);  
        });
    }

    //specify a decider and use it to visit a specified child
    public void visitChild(VisitorDecider decider, int childNumber){
        decider.decideVisitor(children.get(childNumber));
    }

    public void visitChild(VisitorDecider decider, AST child){
        decider.decideVisitor(child);
    }

    //a version of the walk-method that assigns parents to every node that is a child of the root
    public void parentWalk(AST root){
        if (children.size() > 0) {
            for(AST child : root.getChildren()){
                // child.setParent(root);
                parentWalk(child);
            }
        }
    }

    //TODO: these two are currently not functional because of the changes to how scopes work
    /*
    //call this to set the parent scope's value of the variable bound to the given identifier to the value it has in this scope
    public void synthesizeVariable(String identifier){
        parent.getScope().getVTable().put(identifier, scope.getVTable().get(identifier));
    }

    //call this to synthesize all variables that were not declared in this scope
    public void synthesizeInheritedVariables(){
        for (String identifier : scope.getVTable().keySet()) {
            if (!scope.getDTable().contains(identifier)) {  //if we didn't declare a variable with this identifier in this scope
                synthesizeVariable(identifier);             //synthesize it
            }
        }
    }
     */
}