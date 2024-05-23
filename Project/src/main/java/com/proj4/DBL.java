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
import com.proj4.symbolTable.ScopeManager;

public class DBL {
    private Boolean debugMode = false;
    private Boolean verbose = false;
    private String stateTestInput1 = "";
    private String stateTestInput2 = "";
    private LocalTime startTime;

    // Constructor
    public DBL(){}

    public void setStateTestInput(String stateInput1, String stateInput2){
        stateTestInput1 = stateInput1;
        stateTestInput2 = stateInput2;
    }

    // Methods
    public void setDebugMode(Boolean debugMode){
        this.debugMode = debugMode;
    }

    public void setVerbosity(Boolean verbosity){
        this.verbose = verbosity;
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

        String printString = "Reading input: " + input;
        String lines = "";
        for (int i = 0; i < printString.length(); i++) {
            lines += "=";
        }
        System.out.println("\n\n" + lines);
        System.out.println(printString + "\n");

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

            if(this.verbose){
                System.out.println("Lexing and parsing done");
            }
            // Our custom visitor (does the actions as tree is traversed)
            ParseTreeVisitor parseVisitor = new ParseTreeVisitor();

            // Set debug mode for parse tree visitor
            parseVisitor.setDebugMode(this.debugMode);

            // Generate the AST
            parseVisitor.visit(tree);

            // Assign AST
            AST abstractSyntaxTree = parseVisitor.getRoot();
            //Set parents for AST nodes
            abstractSyntaxTree.parentWalk();

            if(this.verbose){
                System.out.println("AST generated");
            }
            if(this.verbose){
                // Print tree
                abstractSyntaxTree.printTree();
            }

            // Set debug status for type checker and interpreter
            ScopeManager.getInstance().setDebugStatus(this.debugMode);

            ScopeManager.getInstance().setVerbosity(this.verbose);

            // Typecheck AST
            CheckDecider checkDecider = new CheckDecider(this.verbose);
            checkDecider.decideVisitor(abstractSyntaxTree);

            // Interpret AST
            InterpreterDecider interpreterDecider = new InterpreterDecider(this.verbose, this.stateTestInput1, this.stateTestInput2);
            interpreterDecider.decideVisitor(abstractSyntaxTree);

            // Input was sucessfully interpreted
            this.printDone(input);
        } catch (Exception e) {
            System.out.println("Failed to interpret input '" + input + "'\n");
            e.printStackTrace();
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
        System.out.println(lines);
        System.out.println(success);
        System.out.printf("Interpreting took %.3f seconds\n", (float) duration.toMillis()/1000);
        System.out.println(lines);
    }
}