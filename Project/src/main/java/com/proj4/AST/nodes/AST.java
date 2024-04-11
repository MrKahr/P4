package com.proj4.AST.nodes;

import java.util.ArrayList;

public class AST {
    //Field
    private ArrayList<AST> children;


    //Constructor
    public AST(){}  // Used at root of parse tree

    public AST(AST node){
        if (children == null) {
            children = new ArrayList<AST>();
        }
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

    // public static void walk(AbstractSyntaxTree AST){
    //     AST.getChildren().forEach((child) -> {AbstractSyntaxTree.walk(child);});
    // }
}