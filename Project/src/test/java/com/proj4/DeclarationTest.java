package com.proj4;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import com.proj4.symbolTable.Scope;
import com.proj4.symbolTable.ScopeObserver;
import com.proj4.symbolTable.symbols.ArraySymbol;
import com.proj4.symbolTable.symbols.BooleanSymbol;
import com.proj4.symbolTable.symbols.IntegerSymbol;
import com.proj4.symbolTable.symbols.StringSymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

public class DeclarationTest extends TestingArgs {
    private static HashMap<String, SymbolTableEntry> variableTable;

    @BeforeAll
    public static void setup() {
        ScopeObserver scopeObserver = new ScopeObserver();
        Scope.addObserver(scopeObserver);

        DBL interpreter = new DBL(true);
        interpreter.interpret(getPath() + "declarationtest.dbl");

        variableTable = scopeObserver.getCurrentScope().peek().getVariableTable();
      }


      @Test
      public void test1(){
        IntegerSymbol integerSymbol = (IntegerSymbol) variableTable.get("a"); // Check variable name
        assertEquals("Integer", integerSymbol.getType()); // Check type
      }


      @Test
      public void test2(){
        StringSymbol stringSymbol = (StringSymbol) variableTable.get("b"); // Check variable name
        assertEquals("String", stringSymbol.getType()); // Check type
      }


      @Test
      public void test3(){
        BooleanSymbol booleanSymbol = (BooleanSymbol) variableTable.get("c"); // Check variable name
        assertEquals("Boolean", booleanSymbol.getType()); // Check type
      }


      @Test
      public void test4(){
        ArraySymbol arraySymbol = (ArraySymbol) variableTable.get("d"); // Check variable name
        assertEquals("Integer", arraySymbol.getType()); // Check type
        assertTrue(arraySymbol.getNestingLevel() == 0); // Check nesting level
      }


      @Test
      public void test5(){
        ArraySymbol arraySymbol = (ArraySymbol) variableTable.get("e"); // Check variable name
        assertEquals("Integer", arraySymbol.getType()); // Check type
        assertTrue(arraySymbol.getNestingLevel() == 0); // Check nesting level
      }
}