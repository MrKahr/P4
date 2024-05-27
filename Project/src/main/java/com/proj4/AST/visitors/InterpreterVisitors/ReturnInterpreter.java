package com.proj4.AST.visitors.InterpreterVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Return;
import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.symbolTable.GlobalScope;
import com.proj4.symbolTable.symbols.TemplateSymbol;

public class ReturnInterpreter implements NodeVisitor {
    private Boolean verbose = false;

    public ReturnInterpreter(){}
    public ReturnInterpreter(Boolean verbose){
        this.verbose = verbose;
    }

    public void visit(AST node) {
        Return returnNode = (Return) node;
        //Note: The return node is always last in the action, so we don't need to keep track of a return address

        //Update return symbol
        returnNode.visitChild(new InterpreterDecider(), returnNode.getReturnValue());

        if(this.verbose){
            System.out.println(this.getClass().getSimpleName() + ": Attempting to write to actionTemplate with identifier \"" + InterpreterVisitor.getInstance().getCurrentActionIdentifier() + "\"");
        }

        //update the action template with the new return symbol
        TemplateSymbol actionTemplate = (TemplateSymbol)GlobalScope.getInstance().getResultTable().get(InterpreterVisitor.getInstance().getCurrentActionIdentifier());

        //the 0th field is RESULT
        actionTemplate.getContent().set(0, InterpreterVisitor.getInstance().getReturnSymbol());
    }
}