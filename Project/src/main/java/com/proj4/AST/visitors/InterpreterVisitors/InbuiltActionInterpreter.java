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
    private Boolean verbose = false;

    public InbuiltActionInterpreter(Boolean verbose){
        this.verbose = verbose;
    }

    public void visit(AST node) {
        ActionCall actionCall = (ActionCall) node;

        if(this.verbose){
            System.out.println(this.getClass().getSimpleName() + ": Interpreting action \"" + actionCall.getIdentifier() + "\"");
        }
        switch (actionCall.getIdentifier()) {
            case "setState":    //sets current the state to the given string
                ActionSymbol setState_ActionSymbol = GlobalScope.getInstance().getActionTable().get(actionCall.getIdentifier());
                StringSymbol setState_ParamSymbol = (StringSymbol) setState_ActionSymbol.getInitialScope().getVariableTable().get("state");
                InterpreterVisitor.getInstance().setCurrentState(setState_ParamSymbol.getValue());

                // Update the action template with the new return symbol
                TemplateSymbol setState_RESULT_Template = (TemplateSymbol) GlobalScope.getInstance().getResultTable().get(actionCall.getIdentifier());
                // The 0th field is RESULT
                setState_RESULT_Template.getContent().set(0, setState_ParamSymbol);
                InterpreterVisitor.getInstance().setReturnSymbol(setState_ParamSymbol);

                if(this.verbose){
                    System.out.println(this.getClass().getSimpleName() + ": Setting state to \"" + setState_ParamSymbol.getValue() + "\"");
                }
                break;
            case "sizeInt":
                ActionSymbol size_ActionSymbol = GlobalScope.getInstance().getActionTable().get(actionCall.getIdentifier());
                ArraySymbol size_ParamSymbol = (ArraySymbol) size_ActionSymbol.getInitialScope().getVariableTable().get("array");
                ArrayList<SymbolTableEntry> paramContent = size_ParamSymbol.getContent();
                Integer contentSize = paramContent.size();

                // Update the action template with the new return symbol
                TemplateSymbol actionTemplate = (TemplateSymbol) GlobalScope.getInstance().getResultTable().get(actionCall.getIdentifier());
                // The 0th field is RESULT

                IntegerSymbol integerSymbol = new IntegerSymbol(contentSize);
                actionTemplate.getContent().set(0, integerSymbol);
                InterpreterVisitor.getInstance().setReturnSymbol(integerSymbol);

                if(this.verbose){
                    System.out.println(this.getClass().getSimpleName() + ": Size of array = "  + contentSize);
                }
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

                // Convert "\n" to newline character
                String printingValue = parameterOne.getValue().replaceAll("\\\\n", "\n");
                System.out.println("OUTPUT: " + printingValue);
            break;
            default:
                throw new UndefinedActionExpection("Action \"" + actionCall.getIdentifier() + "\"is not an inbuilt action!");
        }
    }
}