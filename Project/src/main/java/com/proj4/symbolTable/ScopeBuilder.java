package com.proj4.symbolTable;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import static org.antlr.v4.runtime.CharStreams.fromFileName;

import com.proj4.antlrClass.DBLBaseListener;
import com.proj4.antlrClass.DBLLexer;
import com.proj4.antlrClass.DBLParser;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Stack;

//this class is responsible for creating a queue of scopes which the type checker will use to enforce static type rules.

//step 1: When entering a node, add a scope to a stack.
//A scope consists of a symbol table that binds variables to types and a symbol table that binds functions to types.
//The scope is a union of the node itself and the scope on top of the stack.

//step 2: When exiting a node, pop a scope from the stack and add it to a queue.

//step 3: Pass the queue to the type checker, 
//which will unqueue one scope every time it exits a node and use the symbol tables in it to check that node.

/*
public class Mylistener extends gramBaseListener {
    @Override public void enterEveryRule(ParserRuleContext ctx) {  //see gramBaseListener for allowed functions
        System.out.println("rule entered: " + ctx.getText());      //code that executes per rule
    }
}
 */

public class ScopeBuilder extends DBLBaseListener {

    private Stack<Scope> scopeStack = new Stack<>();
    private LinkedList<Scope> scopeQueue = new LinkedList<>();

    public ScopeBuilder(){
        scopeStack.push(new Scope());   //adding an empty scope as the bottom of the stack. It should never be popped
    }

    //extracts relevant information from an AST-node and creates a new scope containing it
    //relevant information is:
    //the types of newly declared variables
    //the types of newly declared functions
    private Scope setupScope() {
        Scope scope = new Scope();

        return scope;
    }

    //this method combines clones all keys from a new scope and places them in an old scope, 
    //overwriting any duplicates and return the merged scope
    @SuppressWarnings("unchecked")
    private Scope mergeScopes(Scope scopeNew, Scope scopeOld) {
        Scope mergedScope = new Scope();
        //first copy everything from the old scope to the merged scope, and then do the same for the new scope
        mergedScope.getVTable().putAll(scopeOld.getVTable());
        mergedScope.getVTable().putAll(scopeNew.getVTable());

        mergedScope.getFTable().putAll(scopeOld.getFTable());
        mergedScope.getFTable().putAll(scopeNew.getFTable());
        
        return mergedScope;
    }
    /*@Override public void enterProgram(DBLParser.ProgramContext ctx) {
        System.out.println("Program Entered. I should build a global scope here: " + ctx.getText() + "\n");
    }*/

    // @Override public void enterDeclaration(DBLParser.DeclarationContext ctx) {  //see DBLBaseListener for allowed functions
    //     System.out.println("Declaration entered: " + ctx.getText() + "\n");      //code that executes when entering a node in the tree

    //     Scope localScope = new Scope();
    //     //TODO: get information from this node and place it in the scope



    //     localScope = mergeScopes(localScope, scopeStack.peek());
    //     scopeStack.push(localScope);    //combine the local scope with the outer scope and push to the stack
    // }

    @Override public void exitEveryRule(ParserRuleContext ctx) {
        System.out.println("Rule exited: " + ctx.getText() + "\n");      //code that executes when exiting a node in the tree

        scopeQueue.add(scopeStack.pop());
    }
    
    public static void main(String[] args) throws IOException {

        CharStream cs = fromFileName("Project\\inputFiles\\array_test.txt");  //load the file
        DBLLexer lexer = new DBLLexer(cs);  //instantiate a lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer); //scan stream for tokens
        DBLParser parser = new DBLParser(tokens);  //parse the tokens
    
        ParseTree tree = parser.program(); // parse the content and get the tree
        ScopeBuilder listener = new ScopeBuilder();
    
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener,tree);
    }

}