package com.proj4.AST.nodes;
import com.proj4.AST.visitors.NodeVisitor;;

public class ActionDeclNode extends AST {
    private String ID;
    private String type;
    private Object value;


    public ActionDeclNode(){};

    public ActionDeclNode(String type, String ID, Object value){
        this.ID = ID;
        this.type = type;
        this.value = value;
    }

    public ActionDeclNode(String type, String ID){
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