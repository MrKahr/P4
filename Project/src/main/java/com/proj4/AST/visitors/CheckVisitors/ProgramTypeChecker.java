package com.proj4.AST.visitors.CheckVisitors;

import java.util.ArrayList;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.symbolTable.GlobalScope;
import com.proj4.symbolTable.ScopeManager;
import com.proj4.symbolTable.symbols.ActionSymbol;

public class ProgramTypeChecker extends TypeCheckVisitor{

    public void visit(AST program){
        //put inbuilt actions into action table
        ArrayList<String> actionNames = GlobalScope.getInstance().getInbuiltActions();

        for (String actionName : actionNames) {
            ActionSymbol action = new ActionSymbol();
        }

        //start of traversing the AST requires new scope 
        ScopeManager.getInstance().enter();
        program.visitChildren(new CheckDecider());
        ScopeManager.getInstance().exit();
    }
}