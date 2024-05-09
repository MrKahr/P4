package com.proj4;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import com.proj4.symbolTable.Scope;
import com.proj4.symbolTable.ScopeObserver;
import com.proj4.symbolTable.symbols.BooleanSymbol;
import com.proj4.symbolTable.symbols.IntegerSymbol;
import com.proj4.symbolTable.symbols.StringSymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;


public class AssignTest extends TestingArgs {
    private static HashMap<String, SymbolTableEntry> variableTable;

    @BeforeAll
    public static void setup() {
        ScopeObserver scopeObserver = new ScopeObserver();
        Scope.addObserver(scopeObserver);
        Scope.setDebugStatus(true);

        DBL interpreter = new DBL();
        interpreter.interpret(getPath() + "assigntest.dbl");

        variableTable = scopeObserver.getCurrentScope().peek().getVariableTable();
      }


    @Test
    public void test1(){
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("ia");
        assertTrue(intSymbol.getValue() == 3);
    }

    @Test
    public void test2(){
        BooleanSymbol boolSymbol = (BooleanSymbol) variableTable.get("ba");
        System.out.println(boolSymbol.getValue());
        assertTrue(boolSymbol.getValue());
    }

    @Test
    public void test3(){
        StringSymbol stringSymbol = (StringSymbol) variableTable.get("sa");
        System.out.println(stringSymbol.getValue());
        assertEquals("fish_1", stringSymbol.getValue());
    }

    @Test
    public void test4(){
        // TODO: Implement when arrays are done
    }

    @Test
    public void test5(){
        IntegerSymbol intSymbol = (IntegerSymbol) variableTable.get("voa");
        assertTrue(intSymbol.getValue() == 6);
    }
}