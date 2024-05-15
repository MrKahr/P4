package com.proj4;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import com.proj4.symbolTable.Scope;
import com.proj4.symbolTable.ScopeObserver;
import com.proj4.symbolTable.symbols.IntegerSymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

public class IfElseTest extends TestingArgs {
    private static HashMap<String, SymbolTableEntry> variableTable;

    @BeforeAll
    public static void setup() {
        ScopeObserver scopeObserver = new ScopeObserver();
        Scope.addObserver(scopeObserver);
        Scope.setDebugStatus(true);

        DBL interpreter = new DBL(true);
        interpreter.interpret(getPath() + "ifelsetest.dbl");

        variableTable = scopeObserver.getCurrentScope().peek().getVariableTable();
    }

    @Test
    public void test1() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("iea");
        System.out.println(intSymbol.getValue());
        assertTrue(intSymbol.getValue() == 2);
    }
    @Test
    public void test2() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("ieb");
        System.out.println(intSymbol.getValue());
        assertTrue(intSymbol.getValue() == 3);
    }
    @Test
    public void test3() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("iec");
        System.out.println(intSymbol.getValue());
        assertTrue(intSymbol.getValue() == 4);
    }
    @Test
    public void test4() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("ied");
        System.out.println(intSymbol.getValue());
        assertTrue(intSymbol.getValue() == 6);
    }
    @Test
    public void test5() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("iee");
        System.out.println(intSymbol.getValue());
        assertTrue(intSymbol.getValue() == 6);
    }
    @Test
    public void test6() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("ief");
        System.out.println(intSymbol.getValue());
        assertTrue(intSymbol.getValue() == 8);
    }
}
