package com.proj4;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import com.proj4.antlrClass.DBLLexer;
import com.proj4.antlrClass.DBLParser;
/**
 * To use args in VSCode:
 * ctrl+shift+D
 * Create launch.json
 * create configuration with `"args": "arg1 arg2 arg3"`
 * Run and debug with ctrl+shift+D
 * example:
 * {
    "type": "java",
    "name": "Current File Args: test.txt",
    "request": "launch",
    "mainClass": "${file}",
    "projectName": "Project",
    "args": "Project\\inputFiles\\array_test.txt"
* }
 */
public class PrintParseTree {
    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromFileName(args[0]);

        DBLLexer lexer = new DBLLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        DBLParser parser = new DBLParser(tokens);

        ParseTree tree = parser.program();

        System.out.println(tree.toStringTree(parser));
    }
}