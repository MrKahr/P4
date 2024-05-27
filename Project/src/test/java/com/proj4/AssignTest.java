package com.proj4;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import com.proj4.symbolTable.ScopeManager;
import com.proj4.symbolTable.ScopeObserver;
import com.proj4.symbolTable.symbols.BooleanSymbol;
import com.proj4.symbolTable.symbols.IntegerSymbol;
import com.proj4.symbolTable.symbols.StringSymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

public class AssignTest extends TestingArgs {
    private static HashMap<String, SymbolTableEntry> variableTable;

    @BeforeAll
    public static void setup() {
        ScopeObserver scopeObserver = new ScopeObserver();
        ScopeManager.getInstance().addObserver(scopeObserver);

        DBL interpreter = new DBL();
        interpreter.setDebugMode(debugMode);
        interpreter.setVerbosity(verbose);
        interpreter.setShowFinalScope(showFinalScope);
        interpreter.interpret(getPath() + "assigntest.dbl");

        variableTable = scopeObserver.getCurrentScope().peek().getVariableTable();
    }

    @Test
    public void test1() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("ia");
        assertTrue(intSymbol.getValue() == 3);
    }

    @Test
    public void test2() {
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("ba");
        assertTrue(boolSymbol.getValue());
    }

    @Test
    public void test3() {
        StringSymbol stringSymbol = (StringSymbol) variableTable.get("sa");
        assertEquals("fish_1", stringSymbol.getValue());
    }

    @Test
    public void test4() {
        StringSymbol stringSymbol = (StringSymbol) variableTable.get("vb");
        assertEquals("fish_2", stringSymbol.getValue());
    }

    @Test
    public void test5() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("voa");
        assertTrue(intSymbol.getValue() == 6);
    }
}