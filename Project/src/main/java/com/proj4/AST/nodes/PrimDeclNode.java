//TODO: THIS CLASS MUST ONLY BE USED FOR TESTING. IT IS NOT ALLOWED IN THE FINAL AST.

package com.proj4.AST.nodes;
import com.proj4.AST.visitors.NodeVisitor;;

public class PrimDeclNode extends AST {
    private String ID;
    private String type;
    private Object value;


    public PrimDeclNode(){};

    public PrimDeclNode(String type, String ID, Object value){
        this.ID = ID;
        this.type = type;
        this.value = value;
    }

    public PrimDeclNode(String type, String ID){
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