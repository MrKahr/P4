package com.proj4;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.proj4.symbolTable.ScopeManager;
import com.proj4.symbolTable.ScopeObserver;
import com.proj4.symbolTable.symbols.*;

public class StateTest extends TestingArgs{
    private static HashMap<String, SymbolTableEntry> variableTable;

    @BeforeAll
    public static void setup() {
        ScopeObserver scopeObserver = new ScopeObserver();
        ScopeManager.getInstance().addObserver(scopeObserver);

        DBL interpreter = new DBL();
        interpreter.setDebugMode(debugMode);
        interpreter.setVerbosity(verbose);
        TestScanner.getInstance().setTestStatus(true);
        TestScanner.getInstance().provideInput("1\nstate2\n");
        interpreter.interpret(getPath() + "statetest.dbl");
        variableTable = scopeObserver.getCurrentScope().peek().getVariableTable();
    }
    

    @Test
    public void test1() {     
        //test only needs to check that a final state is reached, if test runs and sa exists, we reach a final state
        StringSymbol stringSymbol = (StringSymbol) variableTable.get("state");   
        assertTrue(stringSymbol.getValue().equals("state2"));
    }
}
