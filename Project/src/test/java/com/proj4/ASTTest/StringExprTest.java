package com.proj4.ASTTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;
import com.proj4.AST.nodes.AST;

import com.proj4.ParseTreeVisitor;
import com.proj4.antlrClass.DBLLexer;
import com.proj4.antlrClass.DBLParser;
import com.proj4.antlrClass.DBLParser.ProgramContext;

public class StringExprTest {
    @Test
    public void hasChildrenTest() {
        DBLLexer lexer = new DBLLexer(CharStreams.fromString("String s IS \"John\";\ns IS s + \"'s turn\";"));
        DBLParser parser = new DBLParser(new CommonTokenStream(lexer));
        ProgramContext tree = parser.program();
        ParseTreeVisitor parseVisitor = new ParseTreeVisitor();
        parseVisitor.visit(tree);

        assertTrue(parseVisitor.getRoot().getChild(0).getChild(0).getChild(0).getChildren().size() > 0);
    }
}