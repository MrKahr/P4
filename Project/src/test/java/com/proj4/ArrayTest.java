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
            Integer counter = 1;
            for (SymbolTableEntry entry :  arraySymbol.getContent()) {
                StringSymbol stringSymbol = (StringSymbol) entry;
                assertTrue(stringSymbol.getValue().equals(counter.toString())); // Check values of array
                counter++;
            }
        }

        @Test
        public void test3() {
            ArraySymbol arraySymbol = (ArraySymbol) variableTable.get("c");
            assertEquals("Boolean", arraySymbol.getType()); // Check type
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
            assertTrue(arraySymbol.getNestingLevel() == 2); // Check nesting level
            Integer counter = 1; // Counter for value of innermost array
            for(SymbolTableEntry entry : arraySymbol.getContent()) {
                ArraySymbol arraySymbol2 = (ArraySymbol) entry;
                for(SymbolTableEntry entry2 : arraySymbol2.getContent()){
                    ArraySymbol arraySymbol3 = (ArraySymbol) entry2;
                    for(SymbolTableEntry entry3 : arraySymbol3.getContent()){
                        IntegerSymbol integerSymbol = (IntegerSymbol) entry3;
                        assertTrue(integerSymbol.getValue() == counter);
                        counter++;
                    }
                }
            }
        }

        @Test
        public void test5() { // 3-level nested Integer array - incorrect type
            DBL interpreter = new DBL();
            Executable e = () -> {interpreter.interpret("Integer[] e IS [[[3+5], [\"fisk\"]], [[2], [8]]];");};
            assertThrows(MismatchedTypeException.class, e);
        }

        @Test
        public void test6() {
            ArraySymbol arraySymbol = (ArraySymbol) variableTable.get("e2");
            assertEquals("Integer", arraySymbol.getType()); // Check type
            assertTrue(arraySymbol.getNestingLevel() == 0); // Check nesting level
            IntegerSymbol intSymbol = (IntegerSymbol) arraySymbol.getContent().get(0);
            assertTrue(intSymbol.getValue() == 3); // Check values of array
        }

        @Test
        public void test7() {
            ArraySymbol arraySymbol = (ArraySymbol) variableTable.get("f2");
            assertEquals("Integer", arraySymbol.getType()); // Check type
            assertTrue(arraySymbol.getNestingLevel() == 1); // Check nesting level
            Integer counter = 0; // Counter for value of innermost array
            for (Integer i = 0; i < arraySymbol.getContent().size(); i++) {
                ArraySymbol arraySymbol2 = (ArraySymbol) arraySymbol.getContent().get(i);
                for(Integer j = 0; j < arraySymbol2.getContent().size(); j++){
                    IntegerSymbol integerSymbol = (IntegerSymbol) arraySymbol2.getContent().get(j);
                    assertTrue(integerSymbol.getValue() == counter); // Check values of array
                    counter++;
                }
            }
        }

        @Test
        public void test8() { // Access array out of bounds
            DBL interpreter = new DBL();
            Executable e = () -> {interpreter.interpret("Integer[] g1 IS [1, 2, 3]; Integer[] g2 IS [0]; g2[0] IS g1[3];");};
            assertThrows(MismatchedTypeException.class, e);
        }

        @Test
        public void test9() {
            ArraySymbol arraySymbol = (ArraySymbol) variableTable.get("h1");
            assertEquals("Integer", arraySymbol.getType()); // Check type
            assertTrue(arraySymbol.getNestingLevel() == 0); // Check nesting level
            IntegerSymbol intSymbol = (IntegerSymbol) arraySymbol.getContent().get(3);
            assertTrue(intSymbol.getValue() == 4); // Check value of array
        }
    }
