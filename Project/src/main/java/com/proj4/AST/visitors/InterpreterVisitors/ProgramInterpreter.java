package com.proj4.AST.visitors.InterpreterVisitors;

import java.util.ArrayList;
import java.util.Scanner;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Program;
import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.exceptions.UnsupportedInputException;
import com.proj4.symbolTable.GlobalScope;
import com.proj4.symbolTable.InbuiltActionDefiner;
import com.proj4.symbolTable.ScopeManager;
import com.proj4.symbolTable.symbols.*;



public class ProgramInterpreter implements NodeVisitor {
    private Boolean showFinalScope = false;

    public ProgramInterpreter(Boolean showFinalScope){
        this.showFinalScope = showFinalScope;
    }

    public void visit(AST node) {
        //a scope to put action-templates into
        ScopeManager.getInstance().enter();
/*
        //instantiate action-templates for every action
        for (String identifier : GlobalScope.getInstance().getActionTable().keySet()) {
            if(this.verbose){
                System.out.println("Attempting to instantiate actionTemplate with identifier \"" + identifier + "\"");
            }
            //Create an instance of the action's corresponding template and bind it to the identifier so we can use .RESULT
            ScopeManager.getInstance().getCurrent().declareVariable(
                identifier,
                SymbolTableEntry.instantiateDefault(
                    identifier,
                    "Template",
                    -1
                )
            );
        }
*/
        //interpret the body of the program
        Program program = (Program) node;
        node.visitChildren(new InterpreterDecider());
        //when we're done interpreting the body of the program, begin evaluating states


        Scanner scanner = new Scanner(System.in);
        while (InterpreterVisitor.getInstance().getCurrentState() != null) {
            StateSymbol stateSymbol = GlobalScope.getInstance().getStateTable().get(InterpreterVisitor.getInstance().getCurrentState());
            if (stateSymbol.getBody() != null) {
                program.visitChild(new InterpreterDecider(), stateSymbol.getBody());
            }
            if (stateSymbol.getActionList().size() > 0) {   //prompt the user to start call an action
                //print available actions
                System.out.println("Select action:");
                for (int i = 0; i < stateSymbol.getActionList().size(); i++) {
                    System.out.println("[" + i + "] " + stateSymbol.getActionList().get(i));
                }
                //get input

                int selection = -1;
                try {
                    String input = scanner.nextLine();
                    selection = Integer.valueOf(input);
                } catch (Exception e) {
                    scanner.close();
                    throw new UnsupportedInputException("Error with input, did not read integer:+ " + e.getMessage() );
                }
                // Ensure we can continue on invalid integer input
                if(selection >= stateSymbol.getActionList().size()){
                    System.out.println("Value out of range! Allowable range is 0-" + stateSymbol.getActionList().size());
                    continue;
                }
                String actionName = stateSymbol.getActionList().get(selection);

                //start selected action
                ActionSymbol action = GlobalScope.getInstance().getActionTable().get(stateSymbol.getActionList().get(selection));
                ArrayList<String> parameters = action.getParameterNames();
                System.out.println("Action input parameters are: ");
                for (int index = 0; index < parameters.size(); index++) {
                    System.out.print(action.getInitialScope().getVariableTable().get(parameters.get(index)).getType() +" "+ parameters.get(index)+ ", ");
                }
                for (int index = 0; index < parameters.size(); index++) {
                    System.out.println("Provide input for " + parameters.get(index));
                    String input = "";

                    input = scanner.nextLine();
                    switch (action.getInitialScope().getVariableTable().get(parameters.get(index)).getType()) {
                        case "Integer":
                            action.getInitialScope().getVariableTable().put(parameters.get(index), new IntegerSymbol(Integer.valueOf(input)));
                            break;
                        case "Boolean":
                            action.getInitialScope().getVariableTable().put(parameters.get(index), new BooleanSymbol(Boolean.valueOf(input)));
                            break;
                        case "String":
                            action.getInitialScope().getVariableTable().put(parameters.get(index), new StringSymbol(input));
                            break;
                        default:
                            throw new UnsupportedInputException("Error with input, parameter "+parameters.get(index)+" expected type "+action.getInitialScope().getVariableTable().get(parameters.get(index)).getType()+", which is not supported in state input!");
                    }
                }
                InterpreterVisitor.getInstance().setCurrentAction(stateSymbol.getActionList().get(selection));
                ScopeManager.getInstance().getScopeStack().push(action.getInitialScope());
                if (InbuiltActionDefiner.getDefinerInstance().getIdentifiers().contains(actionName)) {
                    InbuiltActionExecutor.getInstance().executeAction(actionName);
                } else {
                    program.visitChild(new InterpreterDecider(), action.getBody());
                    //finally, trigger any rules bound to his action
                    ArrayList<RuleSymbol> rules = GlobalScope.getInstance().getRuleTable().get(actionName);
                    if (rules != null) {
                        SymbolTableEntry returnSymbol = InterpreterVisitor.getInstance().getReturnSymbol(); // Quick fix to prevent rules from overwriting return symbol of the action call
                        for (RuleSymbol rule : rules) {
                            ScopeManager.getInstance().getScopeStack().push(rule.getInitialScope());
                            program.visitChild(new InterpreterDecider(), rule.getBody());    //pretend the rules' bodies are children
                            ScopeManager.getInstance().exit();
                        }
                        InterpreterVisitor.getInstance().setReturnSymbol(returnSymbol);
                    }
                }
            } else {
                InterpreterVisitor.getInstance().setCurrentState(null);
                System.out.println("Reached final state: No available actions! Stopping program.");
            }
        }
        if(this.showFinalScope){
            if (!ScopeManager.getInstance().getScopeStack().empty()) {
                System.out.println("\nInterpreting done. Final scope: ");
                ScopeManager.getInstance().printBindings(this.showFinalScope);
            } else {
                System.out.println("\nInterpreting done. Final scope is empty!");
            }
            ScopeManager.getInstance().printGlobalScope(GlobalScope.getInstance().getResultTable());
        }
        scanner.close();
        ScopeManager.getInstance().exit();
        GlobalScope.destroyInstance();
    }
}