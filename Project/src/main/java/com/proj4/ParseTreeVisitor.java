package com.proj4;

import com.proj4.antlrClass.DBLBaseVisitor;
import com.proj4.antlrClass.DBLParser;
import com.proj4.antlrClass.DBLParser.ForIContext;
import com.proj4.antlrClass.DBLParser.ForOFContext;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.Collection;

import com.proj4.AST.nodes.*;

// TODO: First example should be a declaration 
// TODO: For ANTLR annotations, check p39 - can make it easier to traverse tree
// TODO: create tree walker printer

public class ParseTreeVisitor extends DBLBaseVisitor<Object> {
    // Fields
    private AST root;

    public AST getRoot() {
        return this.root;
    }

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
        //count is subtracted by one to ignore EOF node in parse tree
        for(int i = 0; i < ctx.getChildCount()-1; i++){
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
    
    @Override
    public ActionDecl visitReturnActionDecl(DBLParser.ReturnActionDeclContext ctx) {
        String resultType = ctx.resultIn().getChild(0).getText();
        ActionDecl node = new ActionDecl(ctx.getChild(1).getText(),resultType);
        for(int i = 0; i < ctx.getChildCount(); i++){
            var childnode = visit(ctx.getChild(i));
            node.addChild((AST)childnode);
        }
        System.out.println(node.getChildren());
        return node; 
    }
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

    @Override 
    public MathExp visitParExpr(DBLParser.ParExprContext ctx){
        return (MathExp) visit(ctx.children.get(1)); 
    }

    @Override 
    public MathExp visitMultExpr(DBLParser.MultExprContext ctx){
        // Create parent 
        MathExp node = new MathExp(MathExpOperator.MULTIPLY,null, null);

        // Add potential children
        node.addChild((AST) visit(ctx.children.get(0))); // Add left expression 
        node.addChild((AST) visit(ctx.children.get(2))); // Add right expression 
        
        return node;
    }

    @Override 
    public MathExp visitAddExpr(DBLParser.AddExprContext ctx){
        // Create parent 
        MathExp node = new MathExp(MathExpOperator.ADD,null, null);

        // Add potential children
        node.addChild((AST) visit(ctx.children.get(0))); // Add left expression 
        node.addChild((AST) visit(ctx.children.get(2))); // Add right expression 
        
        return node;
    }

    @Override 
    public MathExp visitDigitExpr(DBLParser.DigitExprContext ctx){
        MathExp node = new MathExp(MathExpOperator.CONSTANT, null, Integer.parseInt(ctx.DIGIT().getText()));
        return node;
   }

    @Override 
    public MathExp visitIdExpr(DBLParser.IdExprContext ctx){
        MathExp node = new MathExp(MathExpOperator.VARIABLE, ctx.IDENTIFIER().getText(), null);
        return node;
    }

    /*** Declaration ***/
    //@Override  public PrimDeclNode visitIdDeclPrim(DBLParser.IdDeclPrimContext ctx) {rreturn new PrimDeclNode(); 
    //@Override public PrimDeclNode visitAssignDeclPrim(DBLParser.AssignDeclPrimContext ctx) { return new PrimDeclNode(); }
    //@Override public T visitIdDeclUser(DBLParser.IdDeclUserContext ctx) { return visitChildren(ctx); }
    //@Override public T visitAssignDeclUser(DBLParser.AssignDeclUserContext ctx) { return visitChildren(ctx); }

    /*** Return ***/
    // ?????

    /*** RuleDecl ***/
   @Override public RuleDecl visitRuleDecl(DBLParser.RuleDeclContext ctx) {
        // Add if else block 
        RuleDecl node = new RuleDecl((IfElse) visit(ctx.ifBlock()));
        
        // Add all actions 
        for (ParseTree currentNode : ctx.identifierList().children) {
            node.getTriggerActions().add(currentNode.getText());
        }

        return node;
    }

   
    /*** Boolean Expressions ***/
    @Override
    public BoolExp visitGTBool(DBLParser.GTBoolContext ctx){
        BoolExp node = new BoolExp(BoolExpOperator.GREATER_THAN, null, null);
        node.addChild((AST) visit(ctx.children.get(0)));
        node.addChild((AST) visit(ctx.children.get(2)));
        return node;
    }

    @Override
    public BoolExp visitLitteralBool(DBLParser.LitteralBoolContext ctx){
        BoolExp node = new BoolExp(BoolExpOperator.CONSTANT, null, Boolean.getBoolean(ctx.BOOLEAN().getText()));
        return node;
    }

    @Override
    public BoolExp visitAndBool(DBLParser.AndBoolContext ctx){
        BoolExp node = new BoolExp(BoolExpOperator.AND, null, null);
        node.addChild((AST) visit(ctx.children.get(0)));
        node.addChild((AST) visit(ctx.children.get(2)));
        return node;
    }

    @Override
    public BoolExp visitGTOEBool(DBLParser.GTOEBoolContext ctx){
        BoolExp node = new BoolExp(BoolExpOperator.GREATER_OR_EQUALS, null, null);
        node.addChild((AST) visit(ctx.children.get(0)));
        node.addChild((AST) visit(ctx.children.get(2)));
        return node;
    }

    @Override
    public BoolExp visitEqualStringBool(DBLParser.EqualStringBoolContext ctx){
        BoolExpOperator op = ctx.EQUALS() == null ? BoolExpOperator.NOT_EQUALS : BoolExpOperator.EQUALS;
        BoolExp node = new BoolExp(op, null, null);
        node.addChild((AST) visit(ctx.children.get(0)));
        node.addChild((AST) visit(ctx.children.get(2)));
        return node;
    }

    @Override
    public BoolExp visitLTOEBool(DBLParser.LTOEBoolContext ctx){
        BoolExp node = new BoolExp(BoolExpOperator.LESS_OR_EQUALS, null, null);
        node.addChild((AST) visit(ctx.children.get(0)));
        node.addChild((AST) visit(ctx.children.get(2)));
        return node;   
    }

    @Override
    public BoolExp visitIdBool(DBLParser.IdBoolContext ctx){
        BoolExp node = new BoolExp(BoolExpOperator.VARIABLE, ctx.IDENTIFIER().getText(), null);
        return node;
    }

    @Override
    public BoolExp visitNegateBool(DBLParser.NegateBoolContext ctx){
        BoolExp node = new BoolExp(BoolExpOperator.NOT, null, null);
        node.addChild((AST) ctx.children.getLast());
        return node;
    }

    @Override
    public BoolExp visitEqualBool(DBLParser.EqualBoolContext ctx){
        BoolExpOperator op = ctx.EQUALS() == null ? BoolExpOperator.NOT_EQUALS : BoolExpOperator.EQUALS;
        BoolExp node = new BoolExp(op, null, null);
        node.addChild((AST) visit(ctx.children.get(0)));
        node.addChild((AST) visit(ctx.children.get(2)));
        return node;
    }

    @Override
    public BoolExp visitParBool(DBLParser.ParBoolContext ctx){
        return (BoolExp) visit(ctx.children.get(1));
    }

    @Override
    public BoolExp visitLTBool(DBLParser.LTBoolContext ctx){
        BoolExp node = new BoolExp(BoolExpOperator.LESS_THAN, null, null);
        node.addChild((AST) visit(ctx.children.get(0)));
        node.addChild((AST) visit(ctx.children.get(2)));
        return node;
    }

    @Override
    public BoolExp visitOrBool(DBLParser.OrBoolContext ctx){
        BoolExp node = new BoolExp(BoolExpOperator.OR, null, null);
        node.addChild((AST) visit(ctx.children.get(0)));
        node.addChild((AST) visit(ctx.children.get(2)));
        return node;
    }


    /*** For-loops ***/
    @Override
    public ForLoop visitForI(ForIContext ctx) {
        Identifiable iterator = (Identifiable) visit(ctx.children.get(2));
        BoolExp condition = (BoolExp) visit(ctx.children.get(3));
        Assignment iteratorAction = (Assignment) visit(ctx.children.get(5));
        Statement body = (Statement) visit(ctx.children.get(8));

        ForLoop node = new ForLoop(iterator, condition, iteratorAction, body);
        return node;
    }

    @Override
    public Object visitForOF(ForOFContext ctx) {
        // TODO Auto-generated method stub
        return super.visitForOF(ctx);
    }

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
        node.addChild((AST) visit(ctx.stringExpr(0)));
        node.addChild((AST) visit(ctx.stringExpr(1)));
        return node;
    }

    @Override
    public StringExp visitAddStringexpr1(DBLParser.AddStringexpr1Context ctx) {
        StringExp node = new StringExp(StringExpOperator.CONCAT, null, null);
        node.addChild((AST) visit(ctx.stringExpr()));
        node.addChild((AST) visit(ctx.expr()));
        return node;
    }

    @Override
    public StringExp visitAddStringexpr2(DBLParser.AddStringexpr2Context ctx) {
        StringExp node = new StringExp(StringExpOperator.CONCAT, null, null);
        node.addChild((AST) visit(ctx.expr()));
        node.addChild((AST) visit(ctx.stringExpr()));
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
