package com.proj4.AST.visitors.InterpreterVisitors;

import java.util.ArrayList;
import java.util.Collections;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ActionCall;
import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.exceptions.UndefinedActionExpection;
import com.proj4.symbolTable.symbols.*;;

//call this from an ActionCall when trying to interpret a built-in action
//this interpreter uses the same node as ActionCall, so it doesn't have its own, or a spot in the decider
//It's meant for built-in actions that need to do some low-level data manipulation
public class InbuiltFunctionInterpreter implements NodeVisitor {
    public void visit(AST node) {
        ActionCall actionCall = (ActionCall) node;
        switch (actionCall.getIdentifier()) {
            case "setState":    //sets the state to the given string
                actionCall.visitChild(new InterpreterDecider(), actionCall.getChild(0));
                InterpreterVisitor.getInstance().setCurrentState(((StringSymbol)InterpreterVisitor.getInstance().getReturnSymbol()).getValue());
                break;
            case "shuffle":     //randomize the given array
                actionCall.visitChild(new InterpreterDecider(), actionCall.getChild(0));
                Collections.shuffle(((ArraySymbol)InterpreterVisitor.getInstance().getReturnSymbol()).getContent());
                break;
            case "draw":
                actionCall.visitChild(new InterpreterDecider(), actionCall.getChild(0));
                ArrayList<SymbolTableEntry> deck = ((ArraySymbol)InterpreterVisitor.getInstance().getReturnSymbol()).getContent();
                actionCall.visitChild(new InterpreterDecider(), actionCall.getChild(1));
                SymbolTableEntry drawn = deck.remove((int)((IntegerSymbol)InterpreterVisitor.getInstance().getReturnSymbol()).getValue());
                InterpreterVisitor.getInstance().setReturnSymbol(drawn);
                break;
            case "put":
                actionCall.visitChild(new InterpreterDecider(), actionCall.getChild(0)); 
                ArrayList<SymbolTableEntry> targetDeck = ((ArraySymbol)InterpreterVisitor.getInstance().getReturnSymbol()).getContent();
                actionCall.visitChild(new InterpreterDecider(), actionCall.getChild(1));
                targetDeck.add(InterpreterVisitor.getInstance().getReturnSymbol());
                break; 
            default:
                throw new UndefinedActionExpection("Action \"" + actionCall.getIdentifier() + "\"is not an inbuilt action!");
        }
    }
}