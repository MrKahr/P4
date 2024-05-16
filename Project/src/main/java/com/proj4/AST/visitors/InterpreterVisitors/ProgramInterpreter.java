package com.proj4.AST.visitors.InterpreterVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Program;
import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.symbolTable.Scope;
import com.proj4.symbolTable.symbols.ActionSymbol;
import com.proj4.symbolTable.symbols.StateSymbol;
import java.util.Scanner;



public class ProgramInterpreter extends InterpreterVisitor {

    public void visit(AST node) {
        Scope.enter();
        Program program = (Program) node;
        node.visitChildren(new InterpreterDecider());

        //when we're done interpreting the body of the program, begin evaluating states
        while (InterpreterVisitor.getCurrentState() != null) {
            StateSymbol stateSymbol = Scope.getStateTable().get(InterpreterVisitor.getCurrentState());
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
                Scanner inputScan = new Scanner(System.in);
                int selection = inputScan.nextInt();
                //start selected action
                ActionSymbol action = Scope.getActionTable().get(stateSymbol.getActionList().get(selection));
                program.visitChild(new InterpreterDecider(), action.getBody());
                inputScan.close();
            } else {
                InterpreterVisitor.setCurrentState(null);
                System.out.println("Reached final state: No available actions! Stopping program.");
            }
        }

        System.out.println("\nBEFORE");
        if (!Scope.getScopeStack().empty()) {
            Scope.getCurrent().printBindings();
        }
        Scope.exit();

        System.out.println("\nAFTER");
        if (!Scope.getScopeStack().empty()) {
            Scope.getCurrent().printBindings();
        }
    }
}