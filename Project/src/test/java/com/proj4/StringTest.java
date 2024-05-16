package com.proj4;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import com.proj4.symbolTable.Scope;
import com.proj4.symbolTable.ScopeObserver;
import com.proj4.symbolTable.symbols.StringSymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

public class StringTest extends TestingArgs {
    private static HashMap<String, SymbolTableEntry> variableTable;

    @BeforeAll
    public static void setup() {
        ScopeObserver scopeObserver = new ScopeObserver();
        Scope.addObserver(scopeObserver);

        DBL interpreter = new DBL();
        interpreter.setDebugMode(true);
        interpreter.setVerbosity(true);
        interpreter.interpret(getPath() + "stringtest.dbl");

        variableTable = scopeObserver.getCurrentScope().peek().getVariableTable();
    }

    @Test
    public void test1() {
        StringSymbol stringSymbol = (StringSymbol) variableTable.get("sa");
        assertEquals("fish", stringSymbol.getValue());
    }

    @Test
    public void test2() {
        StringSymbol stringSymbol = (StringSymbol) variableTable.get("sb");
        assertEquals("JohnUserman", stringSymbol.getValue());
    }

    @Test
    public void test3() {
        StringSymbol stringSymbol = (StringSymbol) variableTable.get("sc");
        assertEquals("fish1", stringSymbol.getValue());
    }

    @Test
    public void test4() {
        StringSymbol stringSymbol = (StringSymbol) variableTable.get("sd");
        assertEquals("1fish", stringSymbol.getValue());
    }

    @Test
    public void test5() {
        StringSymbol stringSymbol = (StringSymbol) variableTable.get("se");
        assertEquals("😊", stringSymbol.getValue());
    }
}