package com.proj4.systemtest;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.BeforeAll;

import com.proj4.ParseTreeVisitor;
import com.proj4.antlrClass.DBLLexer;
import com.proj4.antlrClass.DBLParser;

public class AssignmentTest {


    @BeforeAll
    public void setup(){
        DBLLexer lexer = new DBLLexer(CharStreams.fromFileName(args[0]));
        DBLParser parser = new DBLParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.program();
        ParseTreeVisitor parseVisitor = new ParseTreeVisitor();
        parseVisitor.visit(tree);
    }


}
