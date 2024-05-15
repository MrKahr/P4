package com.proj4;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import com.proj4.symbolTable.Scope;
import com.proj4.symbolTable.ScopeObserver;
import com.proj4.symbolTable.symbols.IntegerSymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

public class ForLoopTest extends TestingArgs {
    private static HashMap<String, SymbolTableEntry> variableTable;

    @BeforeAll
    public static void setup() {
        ScopeObserver scopeObserver = new ScopeObserver();
        Scope.addObserver(scopeObserver);
        Scope.setDebugStatus(true);

        DBL interpreter = new DBL(true);
        interpreter.interpret(getPath() + "forlooptest.dbl");

        variableTable = scopeObserver.getCurrentScope().peek().getVariableTable();
    }

    @Test
    public void test1() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("fa");
        System.out.println(intSymbol.getValue());
        assertTrue(intSymbol.getValue() == 5);
    }
    @Test
    public void test2() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("fa");
        System.out.println(intSymbol.getValue());
        assertTrue(intSymbol.getValue() == 5);
    }
}
