package com.proj4.symbolTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import com.proj4.symbolTable.symbols.ArraySymbol;
import com.proj4.symbolTable.symbols.BooleanSymbol;
import com.proj4.symbolTable.symbols.IntegerSymbol;
import com.proj4.symbolTable.symbols.NullSymbol;
import com.proj4.symbolTable.symbols.PrimitiveSymbol;
import com.proj4.symbolTable.symbols.StringSymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;
import com.proj4.symbolTable.symbols.TemplateSymbol;

//this class manages the stack of scopes in a DBL-program
public class ScopeManager {
    // Field
    // this class is a singleton:
    private static ScopeManager singleInstance;

    // Flags whether the interpreter should copy scopes to its observers //TODO: Use
    // debug flag to restrict interpreter's printing
    private boolean inDebugMode = false;

    private boolean verbose = false;

    // Stack of scopes
    private Stack<Scope> scopeStack = new Stack<>();

    // TODO: create hashmap of observers instead of arrayList
    private static ArrayList<InterpreterObserver> currentObservers = new ArrayList<InterpreterObserver>();

    // Constructor
    private ScopeManager() {
    }

    // Method
    // get the instance of this class and create it if it doesn't exist yet
    public static ScopeManager getInstance() {
        if (singleInstance == null) {
            singleInstance = new ScopeManager();
        }
        return singleInstance;
    }

    // Stack method wrappers
    public void exit() {
        // We save the current scope if we want to use the scope in testing or debugging
        if (inDebugMode) {
            notifyObservers(copyStack());
        }
        scopeStack.pop();
    }

    public void enter() {
        Scope scope = new Scope();
        scope.setVerbosity(getVerbosity());
        scopeStack.push(scope);
        // We save the current scope if we want to use the scope in testing or debugging
        if (inDebugMode) {
            notifyObservers(copyStack());
        }
    }

    // Scopes inherited are pushed unto the stack because the stack top models the
    // current available scope.
    public void inherit() {
        if (scopeStack.empty()) {
            throw new NullPointerException("Cannot inherit scope - Stack is empty");
        }
        scopeStack.push(scopeStack.peek().clone()); // clone the top scope and push it

        // We save the current scope if we want to use the scope in testing or debugging
        if (inDebugMode) {
            notifyObservers(copyStack());
        }
    }

    public void synthesize() {
        Scope poppedScope = scopeStack.pop();
        Scope currentScope = scopeStack.peek();

        // We save the current scope if we want to use the scope in testing or debugging
        if (inDebugMode) {
            notifyObservers(copyStack());
        }

        for (String identifier : poppedScope.getVariableTable().keySet()) {
            if (!poppedScope.getDeclaredTable().contains(identifier)) {
                currentScope.getVariableTable().put(identifier, poppedScope.getVariableTable().get(identifier));
            }
        }
    }

    public Stack<Scope> getScopeStack() {
        return scopeStack;
    }

    public Scope getCurrent() {
        return scopeStack.peek();
    }

    // OBSERVER PART
    public void addObserver(InterpreterObserver interpreterObserver) {
        currentObservers.add(interpreterObserver);
    }

    public void removeObserver() {
        currentObservers.remove(-1);
    }

    public Stack<Scope> copyStack() {
        Stack<Scope> stackCopy = new Stack<Scope>();
        stackCopy.addAll(scopeStack);
        return stackCopy;
    }

    public void notifyObservers(Stack<Scope> currentScope) {
        for (InterpreterObserver interpreterObserver : currentObservers) {
            interpreterObserver.setCurrentScope(currentScope);
        }
    }

    // DEBUG / VERBOSE part
    public void setDebugStatus(Boolean truthValue) {
        inDebugMode = truthValue;
    }

    public Boolean getDebugStatus() {
        return inDebugMode;
    }

    public void setVerbosity(Boolean verbosity) {
        verbose = verbosity;
    }

    public Boolean getVerbosity() {
        return verbose;
    }

    public void printBindings() {
        if (verbose) {
            System.out.println("----------Bindings----------");
            for (String identifier : getScopeStack().peek().getVariableTable().keySet()) {
                SymbolTableEntry variable = getScopeStack().peek().getVariableTable().get(identifier);
                String value = "";
                if (variable instanceof PrimitiveSymbol) {
                    System.out.println("PRIMITIVE");
                    value = variable instanceof StringSymbol ? "\"" + ((StringSymbol) variable).getValue().toString() + "\"" : ((PrimitiveSymbol) variable).getValue().toString();
                } else if(variable instanceof ArraySymbol) {
                    System.out.println("ARRAY");
                    value += "[";
                    value += parseContent(((ArraySymbol) variable).getContent());
                    value += "]";
                } else if(variable instanceof TemplateSymbol) {
                    System.out.println("TEMPLATE");
                    value = parseContent(((TemplateSymbol) variable).getContent());
                }
                System.out.println(identifier + " |-> " + value + "\n");
            }
            System.out.println("----------Bindings----------");
        }
    }

    public void printGlobalScope(HashMap<String,TemplateSymbol> currentSubtable){
        System.out.println("----------GLOBAL--------------");
        for (String key : currentSubtable.keySet()) {
            System.out.println(key + " |-> " + parseContent(currentSubtable.get(key).getContent()));
        }
        System.out.println("----------GLOBAL--------------");
    }

    private String parseContent(ArrayList<SymbolTableEntry> content) {
        String output = "";
        for(int i = 0; i < content.size(); i++){
            SymbolTableEntry entry = content.get(i);
            if(entry instanceof PrimitiveSymbol){
                output += entry instanceof StringSymbol ? "\"" + ((StringSymbol) entry).getValue() + "\"" : ((PrimitiveSymbol) entry).getValue().toString();
            } else if(entry instanceof ArraySymbol){
                output += "[";
                output += parseContent(((ArraySymbol) entry).getContent());
                output += "]";
            } else if(entry instanceof TemplateSymbol){
                output += parseContent(((TemplateSymbol) entry).getContent());
            }
            if(i < content.size()-1) {
                output += ", ";
            }
        }
        output += "";
        return output != "" ? output : content.toString();
    }
}
