package com.proj4.AST;

import java.util.ArrayList;

public class AST {
    //Field
    private ArrayList<AST> children = new ArrayList<AST>();


    //Constructor
    public AST(){}  // Used at root of parse tree

    public AST(AST node){
        children.add(node);
    }

    //Method
    public ArrayList<AST> getChildren() {
        return children;
    }

    //put a new child on the list of children
    public void addChild(AST newChild){
        children.add(newChild);
    }

    public void walk(AST ast){
        this.getChildren().forEach((child) -> {this.walk(child);});
    }
}