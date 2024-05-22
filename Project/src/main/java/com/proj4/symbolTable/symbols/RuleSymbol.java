package com.proj4.symbolTable.symbols;

import com.proj4.AST.nodes.AST;
import com.proj4.symbolTable.Scope;

public class RuleSymbol{
    //Field
    private AST body;
    private Scope initialScope;

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

    public Scope getInitialScope(){
        return initialScope;
    }

    public void setInitialScope(Scope scope){
        initialScope = scope;
    }
}
