package com.proj4.AST.nodes;

import com.proj4.exceptions.MalformedAstException;

//this class should only be created by the type checker! It exists to tell the interpreter that a typecast must be done
public class StringCast extends AST {
    
    //Constructor
    //creates a StringCast node that inserts itself between the root of the given subtree and its parent
    public StringCast(AST subtree){
        AST parent = subtree.getParent();
        int index = parent.getChildren().indexOf(subtree);
        if (index == -1) {
            //if the subtree can't be found in its parent's children list, something is wrong!
            //this node should be used around expressions, so hopefully this causes no false negatives
            throw new MalformedAstException("Could not find subtree in child list of its parent node when creating StringCast! Is it stored in a separate field?");
        }
        //place this node where the subtree is and remove it
        parent.getChildren().add(index, this);
        parent.getChildren().remove(subtree);
        this.setParent(parent);
        //make the subtree a child of this node and this node its parent
        subtree.setParent(this);
        this.addChild(subtree);
    }

    //Method
    public AST getSubtree(){
        return getChild(0);
    }
}
