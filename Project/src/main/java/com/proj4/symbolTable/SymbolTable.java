/*
package com.proj4.symbolTable;

import java.util.HashMap;
import java.util.Stack;

public class SymbolTable {
    private Stack<HashMap<String, Symbol>> scopes = new Stack<>();
    private HashMap<String, Type> types = new HashMap<>(); // Map of types for user-defined types

    public SymbolTable() {
        // Initialize the global scope
        scopes.push(new HashMap<>());
        types.put("Integer", new PrimitiveType("Integer"));
        types.put("String", new PrimitiveType("String"));
        types.put("Boolean", new PrimitiveType("Boolean"));
    }

    public void enterScope() {
        scopes.push(new HashMap<>());
    }

    public void exitScope() {
        scopes.pop();
    }

    public void define(String name, Type type) {
        if (!scopes.isEmpty()) {
            scopes.peek().put(name, new Symbol(name, type));
        }
    }

    public Symbol resolve(String name) {
        // Looks for symbol
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(name)) {
                return scopes.get(i).get(name);
            }
        }
        return null; // Symbol not found
    }

    public void defineType(String name, Type type) {
        types.put(name, type);
    }

    public Type resolveType(String name) {
        return types.get(name);
    }

    public Stack<HashMap<String, Symbol>> getScopes() {
        return scopes;
    }
}
 */