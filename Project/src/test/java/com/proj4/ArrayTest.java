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
import com.proj4.symbolTable.symbols.BooleanSymbol;
import com.proj4.symbolTable.symbols.IntegerSymbol;
import com.proj4.symbolTable.symbols.StringSymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;


public class ArrayTest extends TestingArgs {
        private static HashMap<String, SymbolTableEntry> variableTable;

        @BeforeAll
        public static void setup() {
            ScopeObserver scopeObserver = new ScopeObserver();
            ScopeManager.getInstance().addObserver(scopeObserver);

            DBL interpreter = new DBL();
            interpreter.setDebugMode(debugMode);
            interpreter.setVerbosity(verbose);
            interpreter.interpret(getPath() + "arraytest.dbl");

            variableTable = scopeObserver.getCurrentScope().peek().getVariableTable();
        }

        @Test
        public void test1() {
            ArraySymbol arraySymbol = (ArraySymbol) variableTable.get("a");
            assertEquals("Integer", arraySymbol.getType()); // Check type
            assertTrue(arraySymbol.getNestingLevel() == 0); // Check nesting level
            for (Integer i = 0; i < arraySymbol.getContent().size(); i++) {
                IntegerSymbol intSymbol = (IntegerSymbol) arraySymbol.getContent().get(i);
                assertTrue(i + 1 == intSymbol.getValue()); // Check values of array
            }
        }

        @Test
        public void test2() {
            ArraySymbol arraySymbol = (ArraySymbol) variableTable.get("b");
            assertEquals("String", arraySymbol.getType()); // Check type
            assertTrue(arraySymbol.getNestingLevel() == 0); // Check nesting level
            for (Integer i = 0; i < arraySymbol.getContent().size(); i++) {
                StringSymbol stringSymbol = (StringSymbol) arraySymbol.getContent().get(i);
                assertTrue(stringSymbol.getValue().equals(i.toString())); // Check values of array
            }
        }

        @Test
        public void test3() {
            ArraySymbol arraySymbol = (ArraySymbol) variableTable.get("c");
            assertEquals("String", arraySymbol.getType()); // Check type
            assertTrue(arraySymbol.getNestingLevel() == 0); // Check nesting level
            for (Integer i = 0; i < arraySymbol.getContent().size(); i++) {
                BooleanSymbol booleanSymbol = (BooleanSymbol) arraySymbol.getContent().get(i);
                assertTrue(booleanSymbol.getValue()); // Check values of array
            }
        }

        @Test
        public void test4() {
            ArraySymbol arraySymbol = (ArraySymbol) variableTable.get("d");
            assertEquals("Integer", arraySymbol.getType()); // Check type
            assertTrue(arraySymbol.getNestingLevel() == 0); // Check nesting level
            for (Integer i = 0; i < arraySymbol.getContent().size(); i++) {
                IntegerSymbol intSymbol = (IntegerSymbol) arraySymbol.getContent().get(i);
                assertTrue(i + 1 == intSymbol.getValue()); // Check values of array
            }
        }

        @Test
        public void test5() { // 3-level nested Integer array - incorrect type
            DBL interpreter = new DBL();
            Executable e = () -> {interpreter.interpret("Integer[] e IS [[[3+5], [\"fisk]], [[2], [8]]];");};
            assertThrows(MismatchedTypeException.class, e);
        }
    }
