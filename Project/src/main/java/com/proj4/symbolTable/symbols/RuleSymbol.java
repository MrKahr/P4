package com.proj4.symbolTable.symbols;

import com.proj4.AST.nodes.AST;

public class RuleSymbol{
    //Field
    private AST body;
    
    //Constructor
    public RuleSymbol(AST body){    //content list is created by default
        this.body = body;
    }    

    //Method
    public AST getBody() {
        return body;
    }

    public String getComplexType(){
        return "Rule";
    }
}
