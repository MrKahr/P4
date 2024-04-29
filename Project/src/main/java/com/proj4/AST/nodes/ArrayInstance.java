package com.proj4.AST.nodes;

public class ArrayInstance extends Expression{
    //Constructor
    public ArrayInstance(){
        //this node is essentially a list of expressions.
        //we use it later to create arrays from that list.
    }
}
//TODO: Note: An array instance is something like "[e_0,...,e_n]" where all e's are expressions with n>=0;
//TODO: an array can consist of subarrays, in which case their nesting level must all be the same