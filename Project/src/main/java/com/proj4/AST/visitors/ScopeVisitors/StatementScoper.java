package com.proj4.AST.visitors.ScopeVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Statement;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.symbolTable.Scope;

public class StatementScoper implements NodeVisitor{
    //Field
    Scope scope;
    //Constructor
    public StatementScoper(Statement parent){
        scope = parent.getScope().clone();    //create a copy of the old scope
    }

    //Method
    public void visit(AST statement){
        
    }
}
