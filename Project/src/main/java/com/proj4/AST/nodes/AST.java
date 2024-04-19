package com.proj4.AST.nodes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.AST.visitors.TestDecider;
import com.proj4.AST.visitors.VisitorDecider;

public abstract class AST {
    //Field
    private ArrayList<AST> children = new ArrayList<AST>();
    private Integer treeLevel = 0;
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

    //put a new child on the list of children
    public void addChild(AST newChild){
        children.add(newChild);
        // newChild.setParent(this);
    }

    // public void setParent(AST parent){
    //     this.parent = parent;
    // }

    public void walk(AST node){
        if(true){    //call recursively if there are children of this node
            System.out.println("Fisk");
            for(AST child : node.getChildren()){
                walk(child);
                System.out.println(child.getClass().getSimpleName());
            }
        }
    }

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

    public void visitChildren(){
        children.forEach((child) -> {
          new TestDecider().decideVisitor(child);  
        });
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
}