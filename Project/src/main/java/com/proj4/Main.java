package com.proj4;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.proj4.antlrClass.DBLLexer;
import com.proj4.antlrClass.DBLParser;

import com.proj4.AST.nodes.*;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.AST.visitors.TestDecider;
import com.proj4.AST.visitors.VisitorDecider;

public class Main {
    public static void main(String[] args) {

        if (args.length == 0) {
            // args = new String[]{"Integer Fisk; String Fisk2;"};
            args = new String[]{
            "Action AttackPlayer(Card attacking, Player attacked) RESULTS IN Player {\n"
            +"    RESULT IN 1+1;\n"
            +"}"};
        }

        System.out.println("Parsing: " + args[0] + "\n");


        // Set args[0] as the input to our lexer
        DBLLexer lexer = new DBLLexer(CharStreams.fromString(args[0]));
        
        // Set the lexed file as input to our parser
        DBLParser parser = new DBLParser(new CommonTokenStream(lexer));
        
        // Create a parse tree. The starting rule is "program"
        ParseTree tree = parser.program();  // THROWS Recognition exception!!!!
        
        // Our custom visitor (does the actions as tree is traversed)
        ParseTreeVisitor parseVisitor = new ParseTreeVisitor();
        
        // We need to implement this:
        parseVisitor.visit(tree);
        // visitor.getAST(); - use this pattern to get tree


        // Sketch test of visitors - REMOVE 
        // Program pn = new Program(null);
        // pn.addChild(new PrimitiveDecl("Integer", "fisk1"));
        // pn.addChild(new PrimitiveDecl("Booleoolean", "fisk2"));
        // //pn.addChild(new MathExp(MathExpOperator.ADD, "fisk", 0));
        // //pn.addChild(new ActionDeclNode("Action", "fisk3"));
        // pn.getChildren().get(0).addChild(new PrimitiveDecl("String", "fisk4"));
        // new TestDecider().decideVisitor(pn);
        // pn.walk(pn);
        // // Sketch test of print parse tree 
        // pn.printTree();
 
    }
}