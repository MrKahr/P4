package com.proj4.AST.visitors.InterpreterVisitors;

import java.util.ArrayList;

import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.exceptions.UndefinedActionExpection;
import com.proj4.symbolTable.GlobalScope;
import com.proj4.symbolTable.symbols.*;

public class InbuiltActionExecutor {

    private InbuiltActionExecutor(){}

    private static InbuiltActionExecutor instance;

    public static InbuiltActionExecutor getInstance(){
        if (instance == null) {
            instance = new InbuiltActionExecutor();
        }
        return instance;
    }

    public void executeAction(String actionName){
        switch (actionName) {
            case "setState":    //sets current the state to the given string
                ActionSymbol setState = GlobalScope.getInstance().getActionTable().get(actionName);
                StringSymbol paramOneState = (StringSymbol) setState.getInitialScope().getVariableTable().get("state");
                InterpreterVisitor.getInstance().setCurrentState(paramOneState.getValue());
                break;
            case "size":
                ActionSymbol size = GlobalScope.getInstance().getActionTable().get(actionName);
                ArraySymbol paramOneArraySymbol = (ArraySymbol) size.getInitialScope().getVariableTable().get("array");
                ArrayList<SymbolTableEntry> array = paramOneArraySymbol.getContent();
                InterpreterVisitor.getInstance().setReturnSymbol(new IntegerSymbol(array.size()));
            case "shuffle":     //randomize the given array
                throw new UnsupportedOperationException("shuffle is not implemented yet.");
            case "draw":
                throw new UnsupportedOperationException("draw is not implemented yet.");
            case "put":
                throw new UnsupportedOperationException("put is not implemented yet.");
            case "write":
                ActionSymbol write = GlobalScope.getInstance().getActionTable().get(actionName);
                StringSymbol parameterOne = (StringSymbol) write.getInitialScope().getVariableTable().get("toWrite");
                System.out.println("OUTPUT: " + parameterOne.getValue());
            break;
            default:
                throw new UndefinedActionExpection("Action \"" + actionName + "\"is not an inbuilt action!");
        }
    }

}
