package com.proj4.AST.nodes;

import java.util.ArrayList;

import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.AST.visitors.VisitorDecider;

public abstract class AST {
    //Field
    private ArrayList<AST> children = new ArrayList<AST>();

    //Method
    public ArrayList<AST> getChildren() {
        return children;
    }

    //put a new child on the list of children
    public void addChild(AST newChild){
        children.add(newChild);
    }

    public void walk(AST node){
        if(children.size() > 0){    //call recursively if there are children of this node
        children.forEach((child) -> {walk(child);});
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