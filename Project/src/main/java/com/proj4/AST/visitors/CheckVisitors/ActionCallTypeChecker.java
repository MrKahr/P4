package com.proj4.AST.visitors.CheckVisitors;

import java.util.ArrayList;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ActionCall;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.MismatchedTypeException;
import com.proj4.exceptions.ParameterMismatchExpection;
import com.proj4.exceptions.UndefinedActionExpection;
import com.proj4.symbolTable.GlobalScope;
import com.proj4.symbolTable.symbols.ActionSymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

public class ActionCallTypeChecker implements NodeVisitor{

    public void visit(AST node){
        ActionCall actionCall = (ActionCall) node;

        ActionSymbol action = GlobalScope.getInstance().getActionTable().get(actionCall.getIdentifier());  //the action to call

        if (action == null) {
            throw new UndefinedActionExpection("Could not find action \"" + actionCall.getIdentifier() + "\"!");
        }

        //check that arguments are the correct amount
        if (action.getParameterNames().size() != actionCall.getChildren().size()) {    //number of parameters must equal number of arguments (child nodes)
            throw new ParameterMismatchExpection("Received " + (actionCall.getChildren().size()) + " arguments for action \"" + actionCall.getIdentifier() + "\" but needed \"" + action.getParameterNames().size() + "\"");
        }

        //check that arguments are well typed
        ArrayList<String> parameterNames = action.getParameterNames();
        for (int index = 0; index < action.getParameterNames().size(); index++) {
            actionCall.visitChild(new CheckDecider(), index);

            SymbolTableEntry parameter = action.getInitialScope().getVariableTable().get(parameterNames.get(index));
          
            if (!TypeCheckVisitor.getFoundType().equals(parameter.getType())){
                throw new MismatchedTypeException("Error for \""+ actionCall.getIdentifier() + "\": Expected type \"" + parameter.getType() + "\"" + " but got \"" + TypeCheckVisitor.getFoundType() + "\"");
            }
            if (!TypeCheckVisitor.getFoundComplexType().equals(parameter.getComplexType())) {
                throw new MismatchedTypeException("Error for \"" + actionCall.getIdentifier() + "\": Expected complex type \"" + parameter.getComplexType() + "\" but got \"" + TypeCheckVisitor.getFoundComplexType() + "\"");
            }
        }

        //arguments are all well typed and we pretend to have gotten a return value

        //System.out.println(this.getClass().getSimpleName() + ": DEBUG ACTION RETURN = " + action.getReturnType());
        TypeCheckVisitor.getInstance().setFoundType(action.getReturnType(), action.getComplexReturnType(), action.getNestingLevel());

    }
}
