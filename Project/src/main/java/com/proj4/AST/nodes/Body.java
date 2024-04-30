package com.proj4.AST.nodes;

//the body-node exists to prevent synthesis of scopes in situations where that would be a problem.
//consider for example the IfElse-node. If it did not use body-nodes,
//a declaration in its then-block would create a variable that is accessible in its else-block.
//we obviously want to avoid this.
public class Body extends Statement{

    //Constructor
    public Body(){}

}
