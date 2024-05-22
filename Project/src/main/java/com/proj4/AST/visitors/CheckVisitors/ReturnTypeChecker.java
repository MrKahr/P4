package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Return;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.MalformedAstException;
import com.proj4.exceptions.MismatchedTypeException;
import com.proj4.symbolTable.GlobalScope;
import com.proj4.symbolTable.symbols.ActionSymbol;

public class ReturnTypeChecker implements NodeVisitor{
    private Boolean verbose = false;

    public ReturnTypeChecker(){}

    public ReturnTypeChecker(Boolean verbose){
        this.verbose = verbose;
    }

    public void visit(AST node){
        Return returnNode = (Return) node;
        String actionIdentifier = TypeCheckVisitor.getInstance().getCurrentAction();


        if(this.verbose) {
            System.out.println(this.getClass().getSimpleName() + ": Attempting to return from \"" + TypeCheckVisitor.getInstance().getCurrentAction() + "\".");
        }

        ActionSymbol action = GlobalScope.getInstance().getActionTable().get(actionIdentifier);

        if (action == null) {
            throw new MalformedAstException("Return-node with undeclared action found!");
        }

        returnNode.visitChild(new CheckDecider(), returnNode.getReturnValue());
        if (!TypeCheckVisitor.getInstance().getFoundType().equals(action.getReturnType())){
            throw new MismatchedTypeException("Error for \""+ actionIdentifier + "\": Mismatched return type! Expected \"" + action.getReturnType() + "\"" + " but got \"" + TypeCheckVisitor.getInstance().getFoundType() + "\"");
        }
        if (!TypeCheckVisitor.getInstance().getFoundComplexType().equals(action.getComplexReturnType())) {
            throw new MismatchedTypeException("Error for \"" + actionIdentifier + "\": Expected complex return type \"" + action.getComplexReturnType() + "\" but got \"" + TypeCheckVisitor.getInstance().getFoundComplexType() + "\"");
        }
        if (TypeCheckVisitor.getInstance().getNestingLevel() != action.getNestingLevel()) {
            throw new MismatchedTypeException("Error for \""+ actionIdentifier + "\": Mismatched return type! Expected nesting level \"" + action.getNestingLevel() + "\" but got \"" + TypeCheckVisitor.getInstance().getNestingLevel() + "\"");
        }
    }
}
