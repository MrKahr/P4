package com.proj4.AST.nodes;
//this class represents the indexing of an array like A[0] or Deck[19]
public class ArrayAccess extends Expression implements Identifiable{
    //Field
    private String identifier;  //the identity that the array is bound to in the symbol table
    
    //Constructor
    public ArrayAccess(String identifier, MathExp index){
        this.identifier = identifier;
        addChild(index);
    }
    
    //Method
    public String getIdentifier(){
        return identifier;
    }

    public MathExp getIndex(){
        return (MathExp) getChildren().get(0);
    }
}
