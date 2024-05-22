package com.proj4;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import com.proj4.symbolTable.ScopeManager;
import com.proj4.symbolTable.ScopeObserver;
import com.proj4.symbolTable.symbols.ArraySymbol;
import com.proj4.symbolTable.symbols.IntegerSymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

public class ActionTest extends TestingArgs {
    private static HashMap<String, SymbolTableEntry> variableTable;

    @BeforeAll
    public static void setup() {
        ScopeObserver scopeObserver = new ScopeObserver();
        ScopeManager.getInstance().addObserver(scopeObserver);

        DBL interpreter = new DBL();
        interpreter.setDebugMode(debugMode);
        interpreter.setVerbosity(verbose);
        interpreter.interpret(getPath() + "actiontest.dbl");

        variableTable = scopeObserver.getCurrentScope().peek().getVariableTable();
    }

    @Test
    public void test1() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("undefined");
        //assertTrue(intSymbol.getValue() != 3);
    }

    @Test
    public void test2() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("vtb");
        assertTrue(intSymbol.getValue() == 6);
    }

    @Test
    public void test3() {
        ArraySymbol arraySymbol = (ArraySymbol) variableTable.get("arr2D");
        assertEquals("Integer", arraySymbol.getType()); // Check type
        assertTrue(arraySymbol.getNestingLevel() == 0); // Check nesting level
        for (Integer i = 0; i < arraySymbol.getContent().size(); i++) {
            ArraySymbol subArr = (ArraySymbol) arraySymbol.getContent().get(i);
            for (Integer j = 0; j < subArr.getContent().size(); j++) {
                IntegerSymbol intSymbol = (IntegerSymbol) arraySymbol.getContent().get(i);
                assertTrue(j + 1 == intSymbol.getValue()); // Check values of array
            }
        }
    }
}
