package com.proj4;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.Duration;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.antlrClass.DBLLexer;
import com.proj4.antlrClass.DBLParser;
import com.proj4.exceptions.MismatchedTypeException;
import com.proj4.symbolTable.Scope;

public class DBL {
    private Boolean debugMode = false;
    private LocalTime startTime;

    // Constructor
    public DBL(){}

    public DBL(Boolean debugMode){
        this.debugMode = debugMode;
    }

    // Methods
    public void setDebugMode(Boolean debugMode){
        this.debugMode = debugMode;
    }

    public Boolean getDebugMode(){
        return this.debugMode;
    }

    /**
     * <b> The interpreter for DBL </b>
     * <p> This class is a wrapper which does the following: </p>
     * <p> 1. Lexing </p>
     * <p> 2. Parsing </p>
     * <p> 3. AST creation </p>
     * <p> 4. Semantic analysis (types, scopes etc.) </p>
     * <p> 5. Interpreting </p>
     * @param input The input to interpret. Can be either a string of DBL-code or a file containing DBL-code
     */
    public void interpret(String input){
        this.startTime = LocalTime.now();

        System.out.println("\nReading input: " + input + "\n");

        File file = new File(input);
        File absFile = new File(file.getAbsolutePath());

        // Create lexer and tokenize input stream depending on type of input (file or string)
        DBLLexer lexer = null;
        try {
            if (absFile.isFile()) {
                lexer = new DBLLexer(CharStreams.fromFileName(file.getAbsolutePath()));
            } else {
                lexer = new DBLLexer(CharStreams.fromString(input));
            }
        } catch (SecurityException se) {
            System.out.println("Access denied to input file '" + file.getAbsolutePath() + "'\n");
            se.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("Encountered an IO error while reading input file '" + file.getAbsolutePath() + "'\n");
            ioe.printStackTrace();
        }
        catch (Exception e) {
            System.out.println("Something bad happened while reading the input\n");
            e.printStackTrace();
        }

        try {
            // Set the lexed file as input to our parser
            DBLParser parser = new DBLParser(new CommonTokenStream(lexer));

            // Create a parse tree. The starting rule is "program"
            ParseTree tree = parser.program();

            System.out.println("Lexing and parsing done");

            // Our custom visitor (does the actions as tree is traversed)
            ParseTreeVisitor parseVisitor = new ParseTreeVisitor();

            // Set debug mode for parse tree visitor
            parseVisitor.setDebugMode(this.debugMode);

            // Generate the AST
            parseVisitor.visit(tree);

            // Assign AST
            AST abstractSyntaxTree = parseVisitor.getRoot();

            System.out.println("AST generated");

            if(this.debugMode){
                // Print tree
                abstractSyntaxTree.printTree();
            }

            // Set debug status for type checker and interpreter
            Scope.setDebugStatus(this.debugMode);

            // Typecheck AST
            CheckDecider checkDecider = new CheckDecider();
            checkDecider.decideVisitor(abstractSyntaxTree);

            // Interpret AST
            InterpreterDecider interpreterDecider = new InterpreterDecider();
            interpreterDecider.decideVisitor(abstractSyntaxTree);

            // Input was sucessfully interpreted
            this.printDone(input);
        } catch (Exception e) {
            System.out.println("Failed to interpret input '" + input + "'\n");
            throw e;
        }
    }

    private void printDone(String input) {
        LocalTime stopTime = LocalTime.now();
        Duration duration = Duration.between(startTime, stopTime);
        String success = "Successfully interpreted input: \"" + input + "\"";
        String lines = "";
        for (int i = 0; i < success.length(); i++) {
            lines += "=";
        }
        System.out.println("\n\n" + lines);
        System.out.println(success);
        System.out.printf("Interpreting took %.3f seconds\n", (float) duration.toMillis()/1000);
        System.out.println(lines);
    }
}