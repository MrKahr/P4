package com.proj4.AST.nodes;   

public class DeclNode extends AST {
    private String ID;
    private String type;
    private Object value;


    public DeclNode(String type, String ID, Object value){
        this.ID = ID;
        this.type = type;
        this.value = value;
    }

    public DeclNode(Object type, String ID){
        this.ID = ID;
        this.type = type;
    }

    public Object getType() {
        return this.type;
    }

    public String getID() {
        return this.ID;
    }

    
}