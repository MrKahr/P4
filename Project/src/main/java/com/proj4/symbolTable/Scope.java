package com.proj4.symbolTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import com.proj4.exceptions.VariableAlreadyDefinedException;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

//this class represents a scope in the programming language
public class Scope implements Cloneable {
    // Field
    private static boolean verbose = false;

    // this table keeps track of variables
    private HashMap<String, SymbolTableEntry> variableTable = new HashMap<>();

    // this table keeps track of whether or not a variable or function has been
    // declared in this scope
    private HashSet<String> declaredTable = new HashSet<>();

    // Inbuilt actions are hard coded
    private static ArrayList<String> inbuiltActions = new ArrayList<>(Arrays.asList("setState", "draw", "shuffle"));

    // these strings are not allowed to be used as identifiers anywhere
    // TODO: the parser seemingly already handles this
    /*
     * private static ArrayList<String> keywords = new
     * ArrayList<>(Arrays.asList("Integer", "Boolean", "String",
     * "Action", "Rule", "State", "Template", "IF", "ELSE IF", "ELSE", "FOR", "AND",
     * "OR", "GREATER THAN", "GREATER OR EQUALS",
     * "LESS THAN", "LESS OR EQUALS", "EQUALS", "NOT EQUALS", "RESULT", "RESULT IN",
     * "RESULTS IN", "CONTAINS", "ALLOWS","WHEN", "WITH LOOP",
     * "IS", "NOT", "NEW"));
     */

    // Method
    public HashMap<String, SymbolTableEntry> getVariableTable() {
        return variableTable;
    }

    public HashSet<String> getDeclaredTable() {
        return declaredTable;
    }

    public void setVariableTable(HashMap<String, SymbolTableEntry> table) {
        variableTable = table;
    }

    public void setDeclaredTable(HashSet<String> table) {
        declaredTable = table;
    }

    // Copy all mappings from the specified scope to this scope, overwriting
    // duplicates with mappings from the other scope
    public void putAll(Scope other) {
        variableTable.putAll(other.getVariableTable());
        // declaredTable.addAll(other.getDTable()); <- don't do this! Otherwise
        // variables must have unique names always
    }

    // clones all the stuff from a scope except for the declaredTable(!)
    @Override
    public Scope clone() {
        Scope clonedScope = new Scope();
        clonedScope.putAll(this);
        return clonedScope;
    }

    public void declareVariable(String identifier, SymbolTableEntry variable) {
        if (declaredTable.contains(identifier)) {
            throw new VariableAlreadyDefinedException("The variable name \"" + identifier + "\" is already in use!");
        } else {
            if (verbose) {
                System.out.println("Declaring variable \"" + identifier + "\" with type \"" + variable.getType()
                        + "\", complex type \"" + variable.getComplexType() + "\".");
            }
            variableTable.put(identifier, variable);
            declaredTable.add(identifier);
        }
    }

    public static ArrayList<String> getInbuiltActions() {
        return inbuiltActions;
    }

    public static void setVerbosity(Boolean verbosity) {
        verbose = verbosity;
    }
}