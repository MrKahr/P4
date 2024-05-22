package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.symbolTable.InbuiltActionDefiner;
import com.proj4.symbolTable.ScopeManager;


public class ProgramTypeChecker implements NodeVisitor{

    public void visit(AST program){
        //put current inbuilt actions into symbol table
        InbuiltActionDefiner.getDefinerInstance().defineActions();
        
        //start of traversing the AST requires new scope 
        ScopeManager.getInstance().enter();
        program.visitChildren(new CheckDecider());
        ScopeManager.getInstance().exit();
    }
}