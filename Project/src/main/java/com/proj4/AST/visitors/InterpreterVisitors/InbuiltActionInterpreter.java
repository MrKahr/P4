package com.proj4.AST.visitors.InterpreterVisitors;

import java.util.ArrayList;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ActionCall;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.exceptions.UndefinedActionExpection;
import com.proj4.symbolTable.GlobalScope;
import com.proj4.symbolTable.symbols.*;;

//call this from an ActionCall when trying to interpret a built-in action
//this interpreter uses the same node as ActionCall, so it doesn't have its own, or a spot in the decider
//It's meant for built-in actions that need to do some low-level data manipulation
public class InbuiltActionInterpreter implements NodeVisitor {
    public void visit(AST node) {
        ActionCall actionCall = (ActionCall) node;
        switch (actionCall.getIdentifier()) {
            case "setState":    //sets current the state to the given string
                ActionSymbol setState = GlobalScope.getInstance().getActionTable().get(actionCall.getIdentifier());
                StringSymbol paramOneState = (StringSymbol) setState.getInitialScope().getVariableTable().get("state");
                InterpreterVisitor.getInstance().setCurrentState(paramOneState.getValue());
                break;
            case "sizeInt":
                ActionSymbol size = GlobalScope.getInstance().getActionTable().get(actionCall.getIdentifier());
                ArraySymbol paramOneArraySymbol = (ArraySymbol) size.getInitialScope().getVariableTable().get("array");
                ArrayList<SymbolTableEntry> array = paramOneArraySymbol.getContent();
                InterpreterVisitor.getInstance().setReturnSymbol(new IntegerSymbol(array.size()));
                break;
            case "shuffle":     //randomize the given array
                throw new UnsupportedOperationException("shuffle is not implemented yet.");
            case "draw":
                throw new UnsupportedOperationException("draw is not implemented yet.");
            case "put":
                throw new UnsupportedOperationException("put is not implemented yet.");
            case "write":
                ActionSymbol write = GlobalScope.getInstance().getActionTable().get(actionCall.getIdentifier());
                StringSymbol parameterOne = (StringSymbol) write.getInitialScope().getVariableTable().get("toWrite");
                System.out.println("OUTPUT: " + parameterOne.getValue());
            break;
            default:
                throw new UndefinedActionExpection("Action \"" + actionCall.getIdentifier() + "\"is not an inbuilt action!");
        }
    }
}