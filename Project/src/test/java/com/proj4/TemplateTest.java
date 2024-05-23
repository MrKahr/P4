package com.proj4;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import com.proj4.symbolTable.ScopeManager;
import com.proj4.symbolTable.ScopeObserver;
import com.proj4.symbolTable.symbols.*;

public class TemplateTest extends TestingArgs {
    private static HashMap<String, SymbolTableEntry> variableTable;

    @BeforeAll
    public static void setup() {
        ScopeObserver scopeObserver = new ScopeObserver();
        ScopeManager.getInstance().addObserver(scopeObserver);

        DBL interpreter = new DBL();
        interpreter.setDebugMode(debugMode);
        interpreter.setVerbosity(verbose);
        interpreter.interpret(getPath() + "templatedecltest.dbl");

        variableTable = scopeObserver.getCurrentScope().peek().getVariableTable();
    }
    @Test
    public void test1() {
            TemplateSymbol card = (TemplateSymbol) variableTable.get("c1");
            IntegerSymbol fieldOne = (IntegerSymbol)card.getContent().get(0);
            IntegerSymbol fieldTwo = (IntegerSymbol)card.getContent().get(1);
            assertTrue(fieldOne.getValue() ==3);
            assertTrue(fieldTwo.getValue() == 2);
    }

    @Test
    public void test2() {
            TemplateSymbol card = (TemplateSymbol) variableTable.get("c1");
            IntegerSymbol fieldOne = (IntegerSymbol)card.getContent().get(0);
            assertTrue(fieldOne.getValue() == 3);
    }
    @Test
    public void test3() {
            IntegerSymbol variable = (IntegerSymbol) variableTable.get("tda");

            assertTrue(variable.getValue() == 3);
    }
    @Test
    public void test4() {
            TemplateSymbol card = (TemplateSymbol) variableTable.get("c2");
            IntegerSymbol fieldOne = (IntegerSymbol)card.getContent().get(0);
            IntegerSymbol fieldTwo = (IntegerSymbol)card.getContent().get(1);
            StringSymbol fieldThree = (StringSymbol)card.getContent().get(2);
            assertTrue(fieldOne.getValue() == 1);
            assertTrue(fieldTwo.getValue() == 2);
            assertTrue(fieldThree.getValue().equals("bob"));
    }

    @Test
    // Nested templates
    public void test5() {
            TemplateSymbol fisk = (TemplateSymbol) variableTable.get("c4");
            TemplateSymbol fieldThree = (TemplateSymbol)fisk.getContent().get(2);

            assertTrue(fieldThree instanceof TemplateSymbol);
    }

}