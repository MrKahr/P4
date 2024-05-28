package com.proj4;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;


public class MunoTest extends TestingArgs{
    @Test
    public void test1() {
        DBL interpreter = new DBL();
        interpreter.setDebugMode(debugMode);
        interpreter.setVerbosity(verbose);
        interpreter.setShowFinalScope(showFinalScope);
        TestScanner.getInstance().setTestStatus(true);
        TestScanner.getInstance().provideInput("0\n1\n0\n3\n0\n5\n0\n3\n0\n6\n0\n0\n0\n8\n0\n0\n0\n2\n0\n0\n");

        Executable e = () -> {interpreter.interpret(getPath() + "muno.dbl");};
        assertDoesNotThrow(e);
    }
}
