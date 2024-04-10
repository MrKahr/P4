package com.proj4.AST;
import com.proj4.antlrClass.DBLBaseVisitor;
import com.proj4.antlrClass.DBLParser;
import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.DeclNode;

// TODO: First example should be a eclaration 
// TODO: For ANTLR annotations, check p39 - can make it easier to traverse tree

public class ASTVisitor extends DBLBaseVisitor<Object>  {
    private AST ast;



    @Override
    public Object visitProgram(DBLParser.ProgramContext ctx) {
        // Here, we should define global scope
        // We want to visit program's children 
        ast = new AST();
        return visitChildren(ctx);
    }


    // @Override
    // public Data visitStmt(DBLParser.StmtContext ctx) {
    //     ctx.for_loop().
    //     // Do stuff
    // }



@Override 
    public Object visitDeclaration(DBLParser.DeclarationContext ctx){
        DeclNode curNode = null;
        visit(ctx.assignment()); // Possible way to check whether element is null

        if(ctx.assignment() != null){
            curNode = new DeclNode(ctx.IDENTIFIER().getText(), ctx.type_primitive().TYPEDEF_PRIMITIVE().getText()); //look in the antlr book to improve "ctx.type_primitive().TYPEDEF_PRIMITIVE().getText()"          System.out.println("Type: " + ctx.type_primitive().getText());
            ast.addChild(curNode);
        }


        
        if(ctx.type_primitive() != null){
            curNode = new DeclNode(ctx.IDENTIFIER().getText(), ctx.type_primitive().TYPEDEF_PRIMITIVE().getText()); //look in the antlr book to improve "ctx.type_primitive().TYPEDEF_PRIMITIVE().getText()"          System.out.println("Type: " + ctx.type_primitive().getText());
            ast.addChild(curNode);
        } else if(ctx.typedef_user() != null) {
            // Implement me
        }
        
        System.out.println("Found declnode. Identifier = " + curNode.getID() + " Type = " + curNode.getType());
        return curNode;
    }


}
