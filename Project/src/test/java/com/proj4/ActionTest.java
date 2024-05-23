package com.proj4;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.BeforeAll;

import com.proj4.exceptions.MismatchedTypeException;
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
            DBL interpreter = new DBL();
            Executable e = () -> {interpreter.interpret("Action ta() {Integer v1 IS 3; Integer v2 IS 4;} Integer varNull IS ta();");};
            assertThrows(MismatchedTypeException.class, e);
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
        assertTrue(arraySymbol.getNestingLevel() == 1); // Check nesting level
        for (Integer i = 0; i < arraySymbol.getContent().size(); i++) {
            ArraySymbol subArr = (ArraySymbol) arraySymbol.getContent().get(i);
            for (Integer j = 0; j < subArr.getContent().size(); j++) {
                IntegerSymbol intSymbol = (IntegerSymbol) arraySymbol.getContent().get(i);
                assertTrue(j + 1 == intSymbol.getValue()); // Check values of array
            }
        }
    }
}
