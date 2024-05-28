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
import com.proj4.symbolTable.symbols.BooleanSymbol;
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
        interpreter.setShowFinalScope(showFinalScope);
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
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("arrsize");
        assertTrue(intSymbol.getValue() == 2);
    }

    @Test
    public void test4() {
        BooleanSymbol booleanSymbol = (BooleanSymbol) variableTable.get("isValid");
        assertFalse(booleanSymbol.getValue());
    }
}
