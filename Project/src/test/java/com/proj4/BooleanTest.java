package com.proj4;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import com.proj4.symbolTable.Scope;
import com.proj4.symbolTable.ScopeObserver;
import com.proj4.symbolTable.symbols.BooleanSymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

public class BooleanTest extends TestingArgs {
    private static HashMap<String, SymbolTableEntry> variableTable;
    @BeforeAll
    public static void setup() {
        ScopeObserver scopeObserver = new ScopeObserver();
        Scope.addObserver(scopeObserver);

        DBL interpreter = new DBL();
        interpreter.setDebugMode(debugMode);
        interpreter.setVerbosity(verbose);
        interpreter.interpret(getPath() + "booleantest.dbl");

        variableTable = scopeObserver.getCurrentScope().peek().getVariableTable();
    }
    @Test
    public void test1(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("ba");
        assertFalse(boolSymbol.getValue());
    }

    @Test
    public void test2(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("bb");
        assertTrue(boolSymbol.getValue());
    }

    @Test
    public void test3(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("bc");
        assertFalse(boolSymbol.getValue());
    }

    @Test
    public void test4(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("bd");
        assertTrue(boolSymbol.getValue());
    }

    @Test
    public void test5(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("be");
        assertTrue(boolSymbol.getValue());
    }

    @Test
    public void test6(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("bf");
        assertTrue(boolSymbol.getValue());
    }

    @Test
    public void test7(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("bg");
        assertFalse(boolSymbol.getValue());
    }

    @Test
    public void test8(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("ea");
        assertTrue(boolSymbol.getValue());
    }

    @Test
    public void test9(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("eb");
        assertFalse(boolSymbol.getValue());
    }

    @Test
    public void test10(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("ec");
        assertTrue(boolSymbol.getValue());
    }

    @Test
    public void test11(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("ed");
        assertFalse(boolSymbol.getValue());
    }

    @Test
    public void test12(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("ee");
        assertTrue(boolSymbol.getValue());
    }

    @Test
    public void test13(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("ef");
        assertFalse(boolSymbol.getValue());
    }

    @Test
    public void test14(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("eg");
        assertTrue(boolSymbol.getValue());
    }

    @Test
    public void test15(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("eh");
        assertFalse(boolSymbol.getValue());
    }

    @Test
    public void test16(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("ei");
        assertTrue(boolSymbol.getValue());
    }

    @Test
    public void test17(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("ej");
        assertFalse(boolSymbol.getValue());
    }

    @Test
    public void test18(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("ek");
        assertTrue(boolSymbol.getValue());
    }

    @Test
    public void test19(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("el");
        assertFalse(boolSymbol.getValue());
    }

    @Test
    public void test20(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("sa");
        assertTrue(boolSymbol.getValue());
    }

    @Test
    public void test21(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("sb");
        assertFalse(boolSymbol.getValue());
    }

    @Test
    public void test22(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("sc");
        assertTrue(boolSymbol.getValue());
    }

    @Test
    public void test23(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("sd");
        assertFalse(boolSymbol.getValue());
    }

    @Test
    public void test24(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("pa");
        assertFalse(boolSymbol.getValue());
    }
}
