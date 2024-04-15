package com.proj4.AST.nodes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.AST.visitors.VisitorDecider;

public abstract class AST {
    //Field
    private ArrayList<AST> children = new ArrayList<AST>();
    private Integer treeLevel = 0;

    //Method
    public ArrayList<AST> getChildren() {
        return children;
    }

    //put a new child on the list of children
    public void addChild(AST newChild){
        children.add(newChild);
    }

    public void walk(AST node){
        if(true){    //call recursively if there are children of this node
        System.out.println("Fisk");
        for(AST child : node.getChildren()){
            walk(child);
            System.out.println(child.getClass().getSimpleName());
        }
        
        //     children.forEach((child) -> {
        // System.out.println(child.getClass().getSimpleName());
        // walk(child);});
        }
    }

    public void printTree(){
        Integer currentLevel = 0;
        List<List<AST>> result = new ArrayList<>();
        getLevel(this, currentLevel, result);
        for (List<AST> list : result) {
            System.out.println("\nLevel " + currentLevel);
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
            if (this.getChildren().size() < 1) {
                return;
            }
            getLevel(ast, level + 1, result);
        }

    }

    public void acceptVisitor(NodeVisitor visitor){
        visitor.visit(this);
    }

    public void visitChildren(){
        children.forEach((child) -> {
          VisitorDecider.decideVisitor(child);  
        });
    }

}