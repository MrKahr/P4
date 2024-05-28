package com.proj4;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import com.proj4.symbolTable.ScopeManager;
import com.proj4.symbolTable.ScopeObserver;
import com.proj4.symbolTable.symbols.IntegerSymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

public class IfElseTest extends TestingArgs {
    private static HashMap<String, SymbolTableEntry> variableTable;

    @BeforeAll
    public static void setup() {
        ScopeObserver scopeObserver = new ScopeObserver();
        ScopeManager.getInstance().addObserver(scopeObserver);

        DBL interpreter = new DBL();
        interpreter.setDebugMode(debugMode);
        interpreter.setVerbosity(verbose);
        interpreter.setShowFinalScope(showFinalScope);
        interpreter.interpret(getPath() + "ifelsetest.dbl");

        variableTable = scopeObserver.getCurrentScope().peek().getVariableTable();
    }

    @Test
    public void test1() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("iea");
        assertTrue(intSymbol.getValue() == 2);
    }
    @Test
    public void test2() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("ieb");
        assertTrue(intSymbol.getValue() == 3);
    }
    @Test
    public void test3() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("iec");
        assertTrue(intSymbol.getValue() == 4);
    }
    @Test
    public void test4() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("ied");
        assertTrue(intSymbol.getValue() == 6);
    }
    @Test
    public void test5() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("iee");
        assertTrue(intSymbol.getValue() == 6);
    }
    @Test
    public void test6() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("ief");
        assertTrue(intSymbol.getValue() == 8);
    }
}
