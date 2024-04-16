package com.proj4;

import com.proj4.antlrClass.DBLBaseVisitor;
import com.proj4.antlrClass.DBLParser;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.Collection;

import com.proj4.AST.nodes.*;

// TODO: First example should be a eclaration 
// TODO: For ANTLR annotations, check p39 - can make it easier to traverse tree
// TODO: create tree walker printer

public class ParseTreeVisitor extends DBLBaseVisitor<Object> {
    // Fields
    AST root;


/*
    ┌──────────────────────────────────┐
    │     Override ANTLR Internals     │
    └──────────────────────────────────┘
*/
    @Override
    /**
     * This method overrides the default implementation of the ANTLR visitor,
     * such that the result returned from a visit to a child node is correctly
     * merged into the result of the parent node.
     */
    public ArrayList<Object> aggregateResult(Object result, Object childResult) {
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

/*
    ┌─────────────────────────────────┐
    │     Set up Root Node of AST     │
    └─────────────────────────────────┘
*/

    @Override
    public Object visitProgram(DBLParser.ProgramContext ctx) {
        // Here, we should define global scope
        // We want to visit program's children

        this.root = new ProgramNode();

        for(int i = 0; i < ctx.getChildCount(); i++){
            var childnode = visit(ctx.getChild(i));
            this.root.addChild((AST)childnode);
        }
        System.out.println("Program Children: " + root.getChildren());
        return root;
    }
    @Override 
    public StmtList visitStmtList(DBLParser.StmtListContext ctx) {
        StmtList node = new StmtList();
        for(int i = 0; i < ctx.getChildCount(); i++){
            var childnode = visit(ctx.getChild(i));
            node.addChild((AST)childnode);
        }
        System.out.println("Stmtlist Children: " + node.getChildren());
        return node;
    }
    @Override 
    public AST visitStmt(DBLParser.StmtContext ctx) {
        AST childnode = (AST) visit(ctx.getChild(0));
        System.out.println("removed parse tree stmt node, handing child AST to parent");
        return childnode;
    }
    
/*
    ┌────────────────────────────────────────────────────────────┐
    │     Override Visitor implementations provided by ANTLR     │
    └────────────────────────────────────────────────────────────┘
*/
    @Override
    public PrimDeclNode visitIdDeclPrim(DBLParser.IdDeclPrimContext ctx) {
        PrimDeclNode node = new PrimDeclNode(ctx.typePrimitive().getText(), ctx.IDENTIFIER().getText());
        System.out.println("returning leaf - IDENTIFIER: " + node.getID() + " | TYPE: " + node.getType());
        // for(int i = 0; i < ctx.getChildCount(); i++){
        //     var childnode = visit(ctx.getChild(i));
        //     System.out.println("Child nr: " +i+ " "+ childnode);
        //     // node.addChild(childnode);
        // }
        // System.out.println(node);
        return node;


    }

    /*** ACTION DECLARATION ***/
    
    // @Override
    // public ActionDecl visitReturnActionDecl(DBLParser.ReturnActionDeclContext ctx) {
    //     ActionDecl node = new ActionDecl();
    //     var children = visitChildren(ctx);
    //     for (AST child : children) {
    //         node.addChild(child);
    //     }
    //     System.out.println(children);
    //     return node; 
    // }
    // @Override
    // public ActionDecl visitNoReturnActionDecl(DBLParser.NoReturnActionDeclContext ctx){
    //     ActionDecl node = new ActionDecl();
    //     var children = visitChildren(ctx);
    //     for (AST child : children) {
    //         node.addChild(child);
    //     }
    //     System.out.println(children);
    //     return node;
    // }

    /*** ACTION CALL ***/

    // @Override public 
    // ActionCall visitActionCall(DBLParser.ActionCallContext ctx) {
    //     var children = visitChildren(ctx);
    //     for (AST child : children) {
    //         node.addChild(child);
    //     }
    //     System.out.println(children);
    //     return node; 
    // }
    @Override
    public ActionCall visitActionCallExpr(DBLParser.ActionCallExprContext ctx) {
        ActionCall node = new ActionCall();
        var children = (ArrayList<Object>) visitChildren(ctx);
        for (Object child : children) {
            node.addChild((AST) child);
        }
        System.out.println(node);
        return node;  
    }
    // @Override public ActionCall visitActionCallAssign(DBLParser.ActionCallAssignContext ctx) {
    //     ActionCall node = new ActionCall();
    //     var children = visitChildren(ctx);
    //     for (AST child : children) {
    //         node.addChild(child);
    //     }
    //     System.out.println(children);
    //     return node;
    // }

    /*** ASSIGNMENT ***/   

	@Override public Assignment visitTemplateAccessAssign(DBLParser.TemplateAccessAssignContext ctx) {
        Assignment node = new Assignment();
        visitChildren(ctx);
        return node; 
    }

	@Override public Assignment visitIdAssign(DBLParser.IdAssignContext ctx) {
        Assignment node = new Assignment();
        visitChildren(ctx);
        return node; 
    }

    /*** ARRAY DECLARATION ***/
    // @Override public ArrayDecl visitArrayDecl(DBLParser.ArrayDeclContext ctx) {
    //     ActionDecl node = new ArrayDecl();
    //     visitChildren(ctx);
    //     return node;  
    // }
    // @Override public ArrayDecl visitDeclarrayDecl(DBLParser.DeclarrayDeclContext ctx) {
    //     ActionDecl node = new ArrayDecl();
    //     visitChildren(ctx);
    //     return node;
    // }

    // /*** ARRAY Access ***/
    // @Override public ArrayAccess visitArrayAccess(DBLParser.ArrayAccessContext ctx) {
    //     ArrayAccess node = new ArrayAccess();
    //     visitChildren(ctx);
    //     return node;
    // }
    // @Override public ArrayAccess visitArrayAccessAssign(DBLParser.ArrayAccessAssignContext ctx) {
    //     ArrayAccess node = new ArrayAccess();
    //     visitChildren(ctx);
    //     return node;
    // }

     /*** MATH EXP ***/
    @Override 
    public MathExp visitMultExpr(DBLParser.MultExprContext ctx){
        System.out.print("Math exp is ");
        System.out.print(" " + ctx.multOp());
        System.out.print(" " + ctx.children);
        System.out.println();

        // Create parent 
        MathExp node = new MathExp(MathExpOperator.MULTIPLY,null, null);

        // Add potential children
        node.addChild((AST) visit(ctx.children.get(0))); // Add left expression 
        node.addChild((AST) visit(ctx.children.get(2))); // Add right expression 
        
        return node;
    }

    @Override 
    public MathExp visitAddExpr(DBLParser.AddExprContext ctx){
        System.out.print("Math exp is ");
        System.out.print(" " + ctx.addOp());
        System.out.print(" " + ctx.children);
        System.out.println();

        // Create parent 
        MathExp node = new MathExp(MathExpOperator.MULTIPLY,null, null);

        // Add potential children
        node.addChild((AST) visit(ctx.children.get(0))); // Add left expression 
        node.addChild((AST) visit(ctx.children.get(2))); // Add right expression 
        
        return node;
    }

    /*** Declaration ***/
    // @Override  public PrimDeclNode visitIdDeclPrim(DBLParser.IdDeclPrimContext ctx) {rreturn new PrimDeclNode(); 
    // @Override public PrimDeclNode visitAssignDeclPrim(DBLParser.AssignDeclPrimContext ctx) { return new PrimDeclNode(); }
    //@Override public T visitIdDeclUser(DBLParser.IdDeclUserContext ctx) { return visitChildren(ctx); }
    //@Override public T visitAssignDeclUser(DBLParser.AssignDeclUserContext ctx) { return visitChildren(ctx); }

    /*** Return ***/

    /*** RuleDecl ***/
   // @Override public RuleDecl visitRuleDecl(DBLParser.RuleDeclContext ctx) {return visitChildren(ctx);}

   
    /*** Bool EXP ***/
    // @Override
    // public BoolExp visitGTBool(DBLParser.GTBoolContext ctx){
        
        
    //     /BoolExp node = new BoolExp(BoolExpOperator.GREATER_THAN, ctx. )
    // }

    // @Override
    // public BoolExp visitLitteralBool(DBLParser.LitteralBoolContext ctx){

    // }

    // @Override
    // public BoolExp visitAndBool(DBLParser.AndBoolContext ctx){

    // }

    // @Override
    // public BoolExp visitGTOEBool(DBLParser.GTOEBoolContext ctx){

    // }

    // @Override
    // public BoolExp visitEqualStringBool(DBLParser.EqualStringBoolContext ctx){

    // }

    // @Override
    // public BoolExp visitLTOEBool(DBLParser.LTOEBoolContext ctx){
        
    // }

    // @Override
    // public BoolExp visitIdBool(DBLParser.IdBoolContext ctx){

    // }

    // @Override
    // public BoolExp visitNegateBool(DBLParser.NegateBoolContext ctx){

    // }

    // @Override
    // public BoolExp visitEqualBool(DBLParser.EqualBoolContext ctx){

    // }

    // @Override
    // public BoolExp visitParBool(DBLParser.ParBoolContext ctx){
        
    // }

    // @Override
    // public BoolExp visitLTBool(DBLParser.LTBoolContext ctx){

    // }

    // @Override
    // public BoolExp visitOrBool(DBLParser.OrBoolContext ctx){
        
    // }


    /*** State declaration ***/
    @Override
    public StateDecl visitIdStateDecl(DBLParser.IdStateDeclContext ctx) {
        StateDecl node = new StateDecl(ctx.typedefUser().getText());
        return node;
    }

    @Override
    public StateDecl visitIdState(DBLParser.IdStateContext ctx) {
        StateDecl node = new StateDecl(ctx.typedefUser().getText());
        return node;
    }

    @Override
    public StateDecl visitIdActionState(DBLParser.IdActionStateContext ctx) {
        StateDecl node = new StateDecl(ctx.typedefUser().getText());
        for(ParseTree treeNode : ctx.actionCall()) {
            node.addChild((AST) visit(treeNode));
        }
        return node;
    }

    /*** String expression ***/
    @Override
    public StringExp visitAddString(DBLParser.AddStringContext ctx) {
        StringExp node = new StringExp(StringExpOperator.CONCAT, null, null);
        Collection children = (Collection) visitChildren(ctx);
        node.addChild((AST) visit(ctx.getChild(0)));
        node.addChild((AST) visit(ctx.getChild(2)));
        return node;
    }

    @Override
    public StringExp visitLitteralString(DBLParser.LitteralStringContext ctx) {
        StringExp node = new StringExp(StringExpOperator.LITERAL, null, ctx.string().getText());
        return node;
    }

    @Override
    public StringExp visitIdString(DBLParser.IdStringContext ctx) {
        StringExp node = new StringExp(StringExpOperator.VARIABLE, ctx.IDENTIFIER().getText(), null);
        return node;
    }

    /*** Templates***/
    @Override
    public TemplateDecl visitTemplateDecl(DBLParser.TemplateDeclContext ctx) {
        TemplateDecl node = new TemplateDecl(ctx.typedefUser().getText());
        for (ParseTree treeNode : ctx.declarationList().declaration()) {
            node.addChild((AST) visit(treeNode));
        }
        return node;
    }

    @Override
    public TemplateInstance visitTemplateInit(DBLParser.TemplateInitContext ctx) {
        TemplateInstance node = new TemplateInstance(ctx.IDENTIFIER().getText(), ctx.typedefUser().getText());
        for(ParseTree treeNode : ctx.children) {
            if(treeNode instanceof DBLParser.TemplateInitContext || treeNode instanceof DBLParser.AssignmentContext) {
                node.addChild((AST) visit(ctx));
            }
        }
        return node;
    }

	/**
	 * Visit a parse tree produced by {@link DBLParser#actionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */

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
