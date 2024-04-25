package com.proj4.AST.visitors.CheckVisitors;

import java.util.ArrayList;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ActionCall;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.MismatchedTypeException;
import com.proj4.exceptions.ParameterMismatchExpection;
import com.proj4.exceptions.UndefinedActionExpection;
import com.proj4.symbolTable.Scope;
import com.proj4.symbolTable.symbols.ActionSymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

public class ActionCallTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        ActionCall actionCall = (ActionCall) node;
        actionCall.inheritScope();

        ActionSymbol action = Scope.getATable().get(actionCall.getIdentifier());  //the action to call

        if (action == null) {
            throw new UndefinedActionExpection("Could not find action \"" + actionCall.getIdentifier() + "\"!");
        }
        
        //check that arguments are the correct amount
        if (action.getContent().size() != actionCall.getChildren().size() - 1) {    //number of parameters must equal number of child nodes minus the body node
            throw new ParameterMismatchExpection("Received" + (actionCall.getChildren().size() - 1) + "arguments for action \"" + actionCall.getIdentifier() + "\" but needed " + action.getContent().size() + "!");
        }

        //check that arguments are well typed
        ArrayList<SymbolTableEntry> params = action.getContent();
        for (int index = 0; index < action.getContent().size(); index++) {
            actionCall.visitChild(new CheckDecider(), index);

            if (!TypeCheckVisitor.getFoundType().equals(params.get(index).getType())){
                throw new MismatchedTypeException("Error for \""+ actionCall.getIdentifier() + "\"Expected type: \"" + params.get(index).getType() + "\"" + "but got \"" + TypeCheckVisitor.getFoundType());                 
            }
            if (!TypeCheckVisitor.getFoundComplexType().equals(params.get(index).getComplexType())) {
                throw new MismatchedTypeException("Error for \"" + actionCall.getIdentifier() + "\" + Expected complex type \"" + params.get(index).getComplexType() + "\" but got \"" + TypeCheckVisitor.getFoundComplexType() + "\"!");
            }
        }

        //arguments are all well typed and we pretend to have gotten a return value
        TypeCheckVisitor.setFoundType(action.getType(), action.getComplexReturnType());

    }
}
