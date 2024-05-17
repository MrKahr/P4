package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Return;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.MalformedAstException;
import com.proj4.exceptions.MismatchedTypeException;
import com.proj4.symbolTable.Scope;
import com.proj4.symbolTable.symbols.ActionSymbol;

public class ReturnTypeChecker extends TypeCheckVisitor{
    private Boolean verbose = false;

    public ReturnTypeChecker(){}

    public ReturnTypeChecker(Boolean verbose){
        this.verbose = verbose;
    }

    public void visit(AST node){
        Return returnNode = (Return) node;
        String actionIdentifier = TypeCheckVisitor.getCurrentAction();

        if(this.verbose) {
            System.out.println(this.getClass().getSimpleName() + ": Attempting to return from \"" + TypeCheckVisitor.getCurrentAction() + "\".");
        }

        ActionSymbol action = Scope.getActionTable().get(actionIdentifier);
        if (action == null) {
            throw new MalformedAstException("Return-node with undeclared action found!");
        }

        returnNode.visitChild(new CheckDecider(), returnNode.getReturnValue());

        if (!TypeCheckVisitor.getFoundType().equals(action.getReturnType())){
            throw new MismatchedTypeException("Error for \""+ actionIdentifier + "\" Mismatched return type! Expected \"" + action.getReturnType() + "\"" + "but got \"" + TypeCheckVisitor.getFoundType());
        }
        if (!TypeCheckVisitor.getFoundComplexType().equals(action.getComplexReturnType())) {
            throw new MismatchedTypeException("Error for \"" + actionIdentifier + "\" + Expected complex return type \"" + action.getComplexReturnType() + "\" but got \"" + TypeCheckVisitor.getFoundComplexType() + "\"!");
        }
        if (TypeCheckVisitor.getNestingLevel() != action.getNestingLevel()) {
            throw new MismatchedTypeException("Error for \""+ actionIdentifier + "\" Mismatched return type! Expected nesting level " + action.getNestingLevel() + " but got " + TypeCheckVisitor.getNestingLevel() + "!");
        }
    }
}
