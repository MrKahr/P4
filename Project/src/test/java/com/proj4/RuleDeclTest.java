package com.proj4;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.proj4.symbolTable.Scope;
import com.proj4.symbolTable.ScopeObserver;
import com.proj4.symbolTable.symbols.IntegerSymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

public class RuleDeclTest extends TestingArgs{
    private static HashMap<String, SymbolTableEntry> variableTable;
    @BeforeAll
    public static void setup() {
        ScopeObserver scopeObserver = new ScopeObserver();
        Scope.addObserver(scopeObserver);

        DBL interpreter = new DBL();
        interpreter.setDebugMode(debugMode);
        interpreter.setVerbosity(verbose);
        interpreter.interpret(getPath() + "ruledecltest.dbl");

        variableTable = scopeObserver.getCurrentScope().peek().getVariableTable();
    }

    @Test
    public void test1() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("ada");
        System.out.println(intSymbol.getValue());
        assertTrue(intSymbol.getValue() == 5);
    }
}