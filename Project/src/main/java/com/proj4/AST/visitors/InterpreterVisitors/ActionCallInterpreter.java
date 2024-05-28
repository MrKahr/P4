package com.proj4.AST.visitors.InterpreterVisitors;

import java.util.ArrayList;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ActionCall;
import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.exceptions.MismatchedTypeException;
import com.proj4.symbolTable.GlobalScope;
import com.proj4.symbolTable.InbuiltActionDefiner;
import com.proj4.symbolTable.ScopeManager;
import com.proj4.symbolTable.symbols.*;

public class ActionCallInterpreter implements NodeVisitor {
    private Boolean verbose = false;

    public ActionCallInterpreter(Boolean verbose){
        this.verbose = verbose;
    }


    public void visit(AST node) {
        ActionCall actionCall = (ActionCall) node;

        ActionSymbol action = GlobalScope.getInstance().getActionTable().get(actionCall.getIdentifier());

        //pass parameters by value
        for (int index = 0; index < action.getParameterNames().size(); index++) {

            actionCall.visitChild(new InterpreterDecider(), index);
            String name = action.getParameterNames().get(index);
            SymbolTableEntry symbol = InterpreterVisitor.getInstance().getReturnSymbol();

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

        //remember thisAction for later in case we're calling an action from another action
        String thisAction = InterpreterVisitor.getInstance().getCurrentActionIdentifier();
        //set the action we're going to call now
        InterpreterVisitor.getInstance().setCurrentAction(actionCall.getIdentifier());
        //prepare the action's scope
        ScopeManager.getInstance().getScopeStack().push(action.getInitialScope());
        
        //check if the action is built-in or not
        if (InbuiltActionDefiner.getDefinerInstance().getIdentifiers().contains(actionCall.getIdentifier())) {
            //hand control to an InbuiltFunctionInterpreter and let it do its thing
            InbuiltActionInterpreter inbuiltActionInterpreter = new InbuiltActionInterpreter(this.verbose);
            inbuiltActionInterpreter.visit(actionCall);
        }
        //pop the action's scope and return to the previous action
        ScopeManager.getInstance().exit();
        InterpreterVisitor.getInstance().setCurrentAction(thisAction);

        //finally, trigger any rules bound to his action
        ArrayList<RuleSymbol> rules = GlobalScope.getInstance().getRuleTable().get(actionCall.getIdentifier());
        if (rules != null) {
            SymbolTableEntry returnSymbol = InterpreterVisitor.getInstance().getReturnSymbol(); // Quick fix to prevent rules from overwriting return symbol of the action call
            for (RuleSymbol rule : rules) {
                //ScopeManager.getInstance().inherit();    //rule evaluation will have dynamic scope rules. Swap these to enter() and exit() for static scope rules
                ScopeManager.getInstance().getScopeStack().push(rule.getInitialScope());
                actionCall.visitChild(new InterpreterDecider(), rule.getBody());    //pretend the rules' bodies are children
                ScopeManager.getInstance().exit();
                //ScopeManager.getInstance().synthesize();
            }
            InterpreterVisitor.getInstance().setReturnSymbol(returnSymbol);
        }
    }
}
