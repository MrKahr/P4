package com.proj4.AST;
import com.proj4.antlrClass.DBLBaseVisitor;
import com.proj4.antlrClass.DBLParser;

// TODO: First example should be a eclaration 

public class ASTVisitor extends DBLBaseVisitor<Data>  {
    

    @Override
    public Data visitProgram(DBLParser.ProgramContext ctx) {
        // Here, we should define global scope



        // We want to visit program's children 
        return visitChildren(ctx);
    }


    // @Override
    // public Data visitStmt(DBLParser.StmtContext ctx) {
    //     ctx.for_loop().
    //     // Do stuff
    // }



@Override 
    public Data visitDeclaration(DBLParser.DeclarationContext ctx){
        String full = ctx.getText();
        ctx.SEMICOLON().getText();
        String assign = ctx.assignment().IDENTIFIER().get(0).getText();

        System.out.println("FOUND Identifier: " + assign);
        
        Object d = new Data(full);
        
        return (Data)d;
    }

}
