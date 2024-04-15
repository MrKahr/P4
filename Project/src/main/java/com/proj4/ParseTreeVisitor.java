package com.proj4;

import com.proj4.antlrClass.DBLBaseVisitor;
import com.proj4.antlrClass.DBLParser;

import java.util.ArrayList;
import java.util.Collection;

import com.proj4.AST.nodes.*;

// TODO: First example should be a eclaration 
// TODO: For ANTLR annotations, check p39 - can make it easier to traverse tree
// TODO: create tree walker printer

public class ParseTreeVisitor extends DBLBaseVisitor<Object> {
    AST root;

    @Override
    public Object aggregateResult(Object result, Object childResult) {
        ArrayList<Object> aggregate = new ArrayList<Object>();
        if (result instanceof Collection) {
            aggregate.addAll((Collection<Object>) result);
        } else {
            aggregate.add(result);
        }
        if (childResult != null)
            aggregate.add(childResult);

        return aggregate;
    }

    @Override
    public Object visitProgram(DBLParser.ProgramContext ctx) {
        // Here, we should define global scope
        // We want to visit program's children

        this.root = new ProgramNode();
        var test = visitChildren(ctx);
        System.out.println(test);

        // this.root.addChild();
        return test;
    }

    @Override
    public PrimDeclNode visitIdDeclPrim(DBLParser.IdDeclPrimContext ctx) {
        PrimDeclNode node = new PrimDeclNode(ctx.type_primitive().getText(), ctx.IDENTIFIER().getText());

        System.out.println("Found IDENTIFIER: " + node.getID() + " | TYPE: " + node.getType());
        return node;
    }

    // @Override
    // public Object visitIdDeclUser(DBLParser.IdDeclUserContext ctx){
    // DeclNode node = new DeclNode(ctx.typedef_user().getText(),
    // ctx.IDENTIFIER().getText());
    // return node;
    // }

    // @Override
    // public Object visitAssignDeclPrim(DBLParser.AssignDeclPrimContext ctx) {
    // // Node does NOT return identifier, but whole assignment statement - CHANGE
    // LINE BELOW
    // DeclNode parent_node = new DeclNode(ctx.type_primitive().getText(),
    // ctx.assignment().getText()); // <--- For testing purposes. This is NOT
    // correct.
    // return parent_node;
    // }
    // //@Override
    // //public Object visitAssignDeclUser(DBLParser.AssignDeclUserContext){}

    // @Override
    // public Object visitDeclaration(DBLParser.DeclarationContext ctx){
    // DeclNode curNode = null;
    // visit(ctx.assignment()); // Possible way to check whether element is null

    // if(ctx.assignment() != null){
    // curNode = new DeclNode(ctx.IDENTIFIER().getText(),
    // ctx.type_primitive().TYPEDEF_PRIMITIVE().getText()); //look in the antlr book
    // to improve "ctx.type_primitive().TYPEDEF_PRIMITIVE().getText()"
    // System.out.println("Type: " + ctx.type_primitive().getText());
    // ast.addChild(curNode);
    // }

    // if(ctx.type_primitive() != null){
    // curNode = new DeclNode(ctx.IDENTIFIER().getText(),
    // ctx.type_primitive().TYPEDEF_PRIMITIVE().getText()); //look in the antlr book
    // to improve "ctx.type_primitive().TYPEDEF_PRIMITIVE().getText()"
    // System.out.println("Type: " + ctx.type_primitive().getText());
    // ast.addChild(curNode);
    // } else if(ctx.typedef_user() != null) {
    // // Implement me
    // }

    // System.out.println("Found declnode. Identifier = " + curNode.getID() + " Type
    // = " + curNode.getType());
    // return curNode;
    // }

}
