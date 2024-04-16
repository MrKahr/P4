package com.proj4.AST.nodes;
//The root of the AST. Can be expanded with globally available attributes if needed
public class Program extends AST{
    
    //Constructor
    public Program(AST child){
        addChild(child);
    }
}
