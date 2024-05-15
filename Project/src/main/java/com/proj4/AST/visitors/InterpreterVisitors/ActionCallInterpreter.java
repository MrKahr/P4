package com.proj4.AST.visitors.InterpreterVisitors;

import java.util.ArrayList;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ActionCall;
import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.exceptions.MismatchedTypeException;
import com.proj4.symbolTable.Scope;
import com.proj4.symbolTable.symbols.*;

public class ActionCallInterpreter extends InterpreterVisitor {

    public void visit(AST node) {
        ActionCall actionCall = (ActionCall) node;

        ActionSymbol action = Scope.getActionTable().get(actionCall.getIdentifier());

        //pass parameters by value
        for (int index = 0; index < action.getParameterNames().size(); index++) {

            actionCall.visitChild(new InterpreterDecider(), index);
            String name = action.getParameterNames().get(index);
            SymbolTableEntry symbol = InterpreterVisitor.getReturnSymbol();

            switch (symbol.getComplexType()) {
                case "Primitive":
                    switch (symbol.getType()) {
                        case "Integer":
                            action.getInitialScope().getVariableTable().put(name, new IntegerSymbol((IntegerSymbol) symbol));
                            break;
                        case "Boolean":
                            action.getInitialScope().getVariableTable().put(name, new BooleanSymbol((BooleanSymbol) symbol));
                            break;
                        case "String":
                            action.getInitialScope().getVariableTable().put(name, new StringSymbol((StringSymbol) symbol));
                            break;
                        default:
                            throw new MismatchedTypeException("Error when interpreting! Got invalid primitive type \"" + symbol.getComplexType() + "\"!");
                    }
                    break;
                case "Template":
                    action.getInitialScope().getVariableTable().put(name, new TemplateSymbol((TemplateSymbol) symbol));
                    break;
                case "Array":
                    action.getInitialScope().getVariableTable().put(name, new ArraySymbol((ArraySymbol) symbol));
                    break;
                default:
                    throw new MismatchedTypeException("Error when interpreting! Got unhandled complex type \"" + symbol.getComplexType() + "\"!");
            }
        }

        //check if the action is built-in or not
        if (Scope.getInbuiltActions().contains(actionCall.getIdentifier())) {
            //hand control to an InbuiltFunctionInterpreter and let it do its thing
            InbuiltFunctionInterpreter inbuiltFunctionInterpreter = new InbuiltFunctionInterpreter();
            inbuiltFunctionInterpreter.visit(actionCall);
        } else {
            //set the scope and interpret the action. Also set the current action so return nodes target the right places
            String thisAction = InterpreterVisitor.getCurrentActionIdentifier();
            InterpreterVisitor.setCurrentAction(actionCall.getIdentifier());
            Scope.getScopeStack().push(action.getInitialScope());
            actionCall.visitChild(new InterpreterDecider(), action.getBody());
            Scope.exit();
            InterpreterVisitor.setCurrentAction(thisAction);
        }
        //finally, trigger any rules bound to his action
        ArrayList<RuleSymbol> rules = Scope.getRuleTable().get(actionCall.getIdentifier());
        if (rules != null) {
            for (RuleSymbol rule : rules) {
                Scope.inherit();    //rule evaluation will have dynamic scope rules. Swap these to enter() and exit() for static scope rules
                actionCall.visitChild(new InterpreterDecider(), rule.getBody());    //pretend the rules' bodies are children
                Scope.synthesize();
            }
        }
    }
}
