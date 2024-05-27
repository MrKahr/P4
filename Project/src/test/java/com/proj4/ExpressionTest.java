package com.proj4;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.function.Executable;

import com.proj4.symbolTable.ScopeManager;
import com.proj4.symbolTable.ScopeObserver;
import com.proj4.symbolTable.symbols.IntegerSymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

public class ExpressionTest extends TestingArgs {
    private static HashMap<String, SymbolTableEntry> variableTable;

    @BeforeAll
    public static void setup() {
        ScopeObserver scopeObserver = new ScopeObserver();
        ScopeManager.getInstance().addObserver(scopeObserver);

        DBL interpreter = new DBL();
        interpreter.setDebugMode(debugMode);
        interpreter.setVerbosity(verbose);
        interpreter.setShowFinalScope(showFinalScope);
        interpreter.interpret(getPath() + "expressiontest.dbl");

        variableTable = scopeObserver.getCurrentScope().peek().getVariableTable();
    }

    @Test
    public void test1() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("ma");
        assertTrue(intSymbol.getValue() == 4);
    }

    @Test
    public void test2() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("mb");
        assertTrue(intSymbol.getValue() == 0);
    }

    @Test
    public void test3() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("mc");
        assertTrue(intSymbol.getValue() == 2);
    }

    // @Test
    // @Disabled
    // public void test4() {
    //     IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("md");
    //     assertTrue(intSymbol.getValue() == -4);
    // }

    // @Test
    // @Disabled
    // public void test5() {
    //     IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("me");
    //     assertTrue(intSymbol.getValue() == 4);
    // }

    @Test
    public void test6() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("da");
        assertTrue(intSymbol.getValue() == 3);
    }

    // @Test
    // @Disabled
    // public void test7() {
    //     IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("db");
    //     assertTrue(intSymbol.getValue() == -3);
    // }

    // @Test
    // @Disabled
    // public void test8() {
    //     IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("dc");
    //     assertTrue(intSymbol.getValue() == 3);
    // }

    @Test
    @DisplayName("Divide by zero")
    public void test9() {
        DBL interpreter = new DBL();
        Executable e = () -> {interpreter.interpret("Integer dd IS 2/0;");};
        assertThrows(ArithmeticException.class, e);
    }

    @Test
    public void test10() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("de");
        assertTrue(intSymbol.getValue() == 1);
    }

    @Test
    public void test11() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("aa");
        assertTrue(intSymbol.getValue() == 4);
    }

    // @Test
    // @Disabled
    // public void test12() {
    //     IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("ab");
    //     assertTrue(intSymbol.getValue() == 0);
    // }

    // @Test
    // @Disabled
    // public void test13() {
    //     IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("ac");
    //     assertTrue(intSymbol.getValue() == -4);
    // }

    @Test
    public void test14() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("sa");
        assertTrue(intSymbol.getValue() == 0);
    }

    @Test
    public void test14_5() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("sa2");
        assertTrue(intSymbol.getValue() == -1);
    }

    // @Test
    // @Disabled
    // public void test15() {
    //     IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("sb");
    //     assertTrue(intSymbol.getValue() == -4);
    // }

    // @Test
    // @Disabled
    // public void test16() {
    //     IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("sb");
    //     assertTrue(intSymbol.getValue() == 0);
    // }

    @Test
    public void test17() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("oa");
        assertTrue(intSymbol.getValue() == 6);
    }

    @Test
    public void test18() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("ob");
        assertTrue(intSymbol.getValue() == 3);

    }

    @Test
    public void test19() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("oc");
        assertTrue(intSymbol.getValue() == 5);
    }

    @Test
    public void test20() {
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("pa");
        assertTrue(intSymbol.getValue() == 9);
    }
}
