package com.proj4.AST.nodes;

import com.proj4.symbolTable.Scope;
public abstract class Statement extends AST {
    
    private Scope scope;        //for now, I'll let every statement have its own scope.
                                //how it's modified and so on remains to be seen.
    public Scope getScope(){
        return scope;
    }

    public void setScope(Scope scope){
        this.scope = scope;
    }
}
