package com.proj4.AST.nodes;   

public class DeclNode extends AST {
    private String ID;
    private String type;

    public DeclNode(String ID, String type){
        this.ID = ID;
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public String getID() {
        return this.ID;
    }
}