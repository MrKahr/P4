package com.proj4.systemtest.src;

import java.io.IOException;

import static org.junit.Assert.*;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import com.proj4.ParseTreeVisitor;
import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.antlrClass.DBLLexer;
import com.proj4.antlrClass.DBLParser;

public class AssignmentTest {
    private InterpreterDecider globalInterpreter;

    @BeforeAll
    public void setup() throws IOException {
        DBLLexer lexer = new DBLLexer(CharStreams.fromFileName("./testfiles/assigntest.txt"));
        DBLParser parser = new DBLParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.program();
        ParseTreeVisitor parseVisitor = new ParseTreeVisitor();
        parseVisitor.visit(tree);
        InterpreterDecider interpreter = new InterpreterDecider();
        interpreter.decideVisitor(parseVisitor.getRoot());
        globalInterpreter = interpreter;
      }

    @Test
    public void exprAssignTest(){

        assertTrue(true);
    }

    @Test
    public void boolExprAssignTest(){

    }

    @Test
    public void stringExprAssignTest(){

    }

    @Test
    public void arrayInitAssignTest(){

    }

    @Test
    public void idAssignTest(){

    }
}