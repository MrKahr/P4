package com.proj4;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.proj4.AST.ASTVisitor;
import com.proj4.antlrClass.DBLLexer;
import com.proj4.antlrClass.DBLParser;

public class Main {
    public static void main(String[] args) {

        if (args.length == 0) {
            args = new String[]{"Integer fisk IS 4;"};
        }

        System.out.println("Parsing: " + args[0]);


        // Set args[0] as the input to our lexer
        DBLLexer lexer = new DBLLexer(CharStreams.fromString(args[0]));
        
        // Set the lexed file as input to our parser
        DBLParser parser = new DBLParser(new CommonTokenStream(lexer));
        
        // Create a parse tree. The starting rule is "program"
        ParseTree tree = parser.program();  // THROWS Recognition exception!!!!
        
        // Our custom visitor (does the actions as tree is traversed)
        ASTVisitor visitor = new ASTVisitor();
        
        // We need to implement this:
        visitor.visit(tree);
    }
}