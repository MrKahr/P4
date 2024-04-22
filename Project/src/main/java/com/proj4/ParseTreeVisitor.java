package com.proj4;

import com.proj4.antlrClass.DBLBaseVisitor;
import com.proj4.antlrClass.DBLParser;
import com.proj4.antlrClass.DBLParser.ArrayInitContext;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
     * ┌──────────────────────────────────┐
     * │     Override ANTLR Internals     │
     * └──────────────────────────────────┘
     */
    
    /**
     * This method overrides the default implementation of the ANTLR visitor,
     * such that the result returned from a visit to a child node is correctly
     * merged into the result of the parent node.
     */
    // @Override
    // public ArrayList<Object> aggregateResult(Object result, Object childResult) {
    //     ArrayList<Object> aggregate = new ArrayList<Object>();
    //     if (result instanceof Collection) {
    //         aggregate.addAll((Collection<Object>) result);
    //     } else {
    //         aggregate.add(result);
    //     }
    //     if (childResult != null)
    //         aggregate.add(childResult);

    //     return aggregate;
    // }

    /*
     * ┌─────────────────────────────────┐
     * │     Set up Root Node of AST     │
     * └─────────────────────────────────┘
     */

    @Override
    public Object visitProgram(DBLParser.ProgramContext ctx) {
        // Here, we should define global scope
        // We want to visit program's children

        this.root = new Program();
        // count is subtracted by one to ignore EOF node in parse tree
        for (int i = 0; i < ctx.getChildCount() - 1; i++) {
            var childnode = visit(ctx.getChild(i));
            this.root.addChild((AST) childnode);
        }
        System.out.println("Program Children: " + root.getChildren());
        return root;
    }

    @Override
    public StmtList visitStmtList(DBLParser.StmtListContext ctx) {
        StmtList node = new StmtList();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            var childnode = visit(ctx.getChild(i));
            if (childnode != null) {
                node.addChild((AST) childnode);
            }
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
     * ┌────────────────────────────────────────────────────────────┐
     * │    Override Visitor implementations provided by ANTLR      │
     * └────────────────────────────────────────────────────────────┘
     */
    @Override
    public PrimitiveDecl visitIdDeclPrim(DBLParser.IdDeclPrimContext ctx) {
        PrimitiveDecl node = new PrimitiveDecl(ctx.typePrimitive().getText(), ctx.IDENTIFIER().getText());
        // for(int i = 0; i < ctx.getChildCount(); i++){
        // var childnode = visit(ctx.getChild(i));
        // System.out.println("Child nr: " +i+ " "+ childnode);
        // // node.addChild(childnode);
        // }
        // System.out.println(node);
        return node;

    }

    /*** ACTION DECLARATION ***/

    @Override
    public ActionDecl visitReturnActionDecl(DBLParser.ReturnActionDeclContext ctx) {
        String resultType = ctx.resultsIn().getChild(1).getText();
        String identifier = ctx.getChild(1).getText();
        ActionDecl node = new ActionDecl(identifier, resultType);
        System.out.println("Action() " + node.getIdentifier() + " RESULTS IN " + node.getType());
        for (int i = 0; i < ctx.getChildCount(); i++) {
            var parseChild = ctx.getChild(i);
            var childnode = visit(parseChild);
            if (childnode != null) {
                // index 3 contains the parameter list
                if (i == 3) {
                    for (UserDeclNode parameter : (ArrayList<UserDeclNode>) childnode) {
                        node.addChild((AST) parameter);
                    }
                } else {
                    node.addChild((AST) childnode);
                }
            }
        }
        
        System.out.println("children actionDecl: "+node.getChildren());
        return node;
    }
    @Override
    public ActionDecl visitNoReturnActionDecl(DBLParser.NoReturnActionDeclContext ctx){
        String identifier = ctx.getChild(1).getText();
        ActionDecl node = new ActionDecl(identifier);
        for (int i = 0; i < ctx.getChildCount(); i++) {
            var childnode = visit(ctx.getChild(i));
            node.addChild((AST) childnode);
        }
        System.out.println(node.getChildren());
        return node;
    }

    @Override 
    public ArrayList< UserDeclNode> visitParameterList(DBLParser.ParameterListContext ctx) {
        ArrayList<UserDeclNode> parameterNodes = new ArrayList< UserDeclNode>();
        String identifier = new String();
        String type = new String();
        for (int i = 0; i < ctx.getChildCount(); i++){
            if((i+1)%3 == 1){
                identifier = ctx.getChild(i).getText();
            }
            if((i+1)%3 == 2){
                type = ctx.getChild(i).getText();
                UserDeclNode node = new UserDeclNode(identifier, type);
                parameterNodes.add(node);
            }
        }
        return parameterNodes; 
    }
	
    /*** ACTION CALL ***/

    @Override
    public ActionCall visitActionCall(DBLParser.ActionCallContext ctx) {
        String identifier = ctx.getChild(0).getText();
        ActionCall node = new ActionCall(identifier);
        for (int i = 0; i < ctx.getChildCount(); i++) {
            var childnode = visit(ctx.getChild(i));
            node.addChild((AST) childnode);
        }
        System.out.println(node);
        return node;
    }
    @Override
    public ActionCall visitActionCallExpr(DBLParser.ActionCallExprContext ctx) {
        String identifier = ctx.getChild(0).getText();
        ActionCall node = new ActionCall(identifier);
        for (int i = 0; i < ctx.getChildCount(); i++) {
            var childnode = visit(ctx.getChild(i));
            node.addChild((AST) childnode);
        }
        System.out.println(node);
        return node;
    }
    @Override
    public Assignment visitActionCallAssign(DBLParser.ActionCallAssignContext ctx) {
        Assignment node;
        if (ctx.templateAccess().isEmpty()){
            node = new Assignment(ctx.IDENTIFIER().getText()); // Get left side of assignment
        } else {
            TemplateAccess tAccess = (TemplateAccess) visit(ctx.templateAccess());  // Get left side of assignment
            node = new Assignment(null);
            node.addChild(tAccess);
        }
        node.addChild((ActionCall) visit(ctx.actionCall()));
        return node;
    }

    /*** ASSIGNMENT ***/
    @Override
    public Assignment visitTemplateAccessAssign(DBLParser.TemplateAccessAssignContext ctx) {
        Assignment node;
        if (ctx.templateAccess().size() > 1){
            node = new Assignment(null);
            node.addChild((TemplateAccess) visit(ctx.templateAccess().get(0))); // Get left side of assignment
        } else {
            node = new Assignment(ctx.IDENTIFIER().getText());  // Get left side of assignment
        }
        node.addChild((TemplateAccess) visit(ctx.templateAccess().get(2))); // Get right side of assignment
        return node;
    }

    @Override
    public Assignment visitIdAssign(DBLParser.IdAssignContext ctx) {
        Assignment node;
        if (ctx.templateAccess().isEmpty()){
            node = new Assignment(ctx.IDENTIFIER().get(0).getText()); // Get left side of assignment
        } else {
            TemplateAccess tAccess = (TemplateAccess) visit(ctx.templateAccess());  // Get left side of assignment
            node = new Assignment(null);
            node.addChild(tAccess);
        }
        node.addChild(new Variable(ctx.IDENTIFIER().get(2).getText()));
        return node;
    }

    @Override
    public Assignment visitExprAssign(DBLParser.ExprAssignContext ctx) {
        Assignment node;
        if (ctx.templateAccess().isEmpty()){
            node = new Assignment(ctx.IDENTIFIER().getText()); // Get left side of assignment
        } else {
            TemplateAccess tAccess = (TemplateAccess) visit(ctx.templateAccess());  // Get left side of assignment
            node = new Assignment(null);
            node.addChild(tAccess);
        }
        node.addChild((MathExp) visit(ctx.expr()));
        return node;
    }

    @Override
    public Assignment visitBoolExprAssign(DBLParser.BoolExprAssignContext ctx) {
        Assignment node;
        if (ctx.templateAccess().isEmpty()){
            node = new Assignment(ctx.IDENTIFIER().getText()); // Get left side of assignment
        } else {
            TemplateAccess tAccess = (TemplateAccess) visit(ctx.templateAccess());  // Get left side of assignment
            node = new Assignment(null);
            node.addChild(tAccess);
        }
        node.addChild((BoolExp) visit(ctx.boolExpr()));
        return node;        
    }

    @Override
    public Assignment visitStringExprAssign(DBLParser.StringExprAssignContext ctx) {
        Assignment node;
        if (ctx.templateAccess().isEmpty()){
            node = new Assignment(ctx.IDENTIFIER().getText()); // Get left side of assignment
        } else {
            TemplateAccess tAccess = (TemplateAccess) visit(ctx.templateAccess());  // Get left side of assignment
            node = new Assignment(null);
            node.addChild(tAccess);
        }
        node.addChild((StringExp) visit(ctx.stringExpr()));
        return node;     
    }

    /*** ARRAY DECLARATION ***/
    @Override
    /**
     * Declaration of an array - can happen everywhere
     */
    public ArrayDecl visitArrayDecl(DBLParser.ArrayDeclContext ctx) {
        String valueType = ctx.getChild(0).getText();
        String identifier = ctx.getChild(3).getText();
        ArrayDecl node = new ArrayDecl(identifier, valueType);
        for (int i = 0; i < ctx.getChildCount(); i++) {
            var childnode = visit(ctx.getChild(i));
            node.addChild((AST) childnode);
        }
        System.out.println(node.getChildren());
        return node;
    }
    @Override
    /**
     * Declaration of an array - this instance is declared alone, i.e. on a line for itself
     */
    public ArrayDecl visitDeclArrayDecl(DBLParser.DeclArrayDeclContext ctx) {
        String valueType = ctx.getChild(0).getText();
        String identifier = ctx.getChild(3).getText();
        ArrayDecl node = new ArrayDecl(identifier, valueType);
        for (int i = 0; i < ctx.getChildCount(); i++) {
            var childnode = visit(ctx.getChild(i));
            node.addChild((AST) childnode);
        }
        System.out.println(node.getChildren());
        return node;
    }

    // /*** ARRAY Access ***/
    @Override
    public ArrayAccess visitArrayAccess(DBLParser.ArrayAccessContext ctx) {
        String identifier = ctx.getChild(0).getText();
        ArrayAccess node = new ArrayAccess(identifier);
        for (int i = 0; i < ctx.getChildCount(); i++) {
            var childnode = visit(ctx.getChild(i));
            node.addChild((AST) childnode);
        }
        System.out.println(node.getChildren());
        return node;
    }

    @Override
    public Object visitArrayInit(ArrayInitContext ctx) {
        
    }


    @Override
        public Assignment visitArrayAccessAssign(DBLParser.ArrayAccessAssignContext ctx) {
            Assignment node;
            if (ctx.templateAccess().isEmpty()){
                node = new Assignment(ctx.IDENTIFIER().getText()); // Get left side of assignment
            } else {
                TemplateAccess tAccess = (TemplateAccess) visit(ctx.templateAccess());  // Get left side of assignment
                node = new Assignment(null);
                node.addChild(tAccess);
            }
            node.addChild((ArrayAccess) visit(ctx.arrayAccess()));
            return node;
    }

    @Override
    public Assignment visitArrayInitAssign(ArrayInitAssignContext ctx) {
        Assignment node;
        if (ctx.templateAccess().isEmpty()){
            node = new Assignment(ctx.IDENTIFIER().getText()); // Get left side of assignment
        } else {
            TemplateAccess tAccess = (TemplateAccess) visit(ctx.templateAccess());  // Get left side of assignment
            node = new Assignment(null);
            node.addChild(tAccess);
        }
        node.addChild((ArrayInit) visit(ctx.arrayInit()));
    }

    @Override
    public MathExp visitParExpr(DBLParser.ParExprContext ctx) {
        return (MathExp) visit(ctx.children.get(1));
    }

    @Override
    public MathExp visitMultExpr(DBLParser.MultExprContext ctx) {
        // Create parent
        MathExp node = new MathExp(MathExpOperator.MULTIPLY, null);

        // Add potential children
        node.addChild((AST) visit(ctx.children.get(0))); // Add left expression
        node.addChild((AST) visit(ctx.children.get(2))); // Add right expression

        return node;
    }

    @Override
    public MathExp visitAddExpr(DBLParser.AddExprContext ctx) {
        // Create parent
        MathExp node = new MathExp(MathExpOperator.ADD, null);

        // Add potential children
        node.addChild((AST) visit(ctx.children.get(0))); // Add left expression
        node.addChild((AST) visit(ctx.children.get(2))); // Add right expression
        return node;
    }

    @Override
    public MathExp visitDigitExpr(DBLParser.DigitExprContext ctx) {
        MathExp node = new MathExp(MathExpOperator.CONSTANT, Integer.parseInt(ctx.DIGIT().getText()));
        return node;
    }

    @Override
    public Variable visitIdExpr(DBLParser.IdExprContext ctx) {
        Variable node = new Variable(ctx.IDENTIFIER().getText());
        return node;
    }

    /*** Declaration ***/
    // @Override public PrimDeclNode visitIdDeclPrim(DBLParser.IdDeclPrimContext
    // ctx) {rreturn new PrimDeclNode();
    // @Override public PrimDeclNode
    // visitAssignDeclPrim(DBLParser.AssignDeclPrimContext ctx) { return new
    // PrimDeclNode(); }

    @Override
    public UserDeclNode visitIdDeclUser(DBLParser.IdDeclUserContext ctx) {
        UserDeclNode node = new UserDeclNode(ctx.typedefUser().getText(), ctx.IDENTIFIER().getText());
        return node;
    }

    /*** MADS START HERE***/
    @Override
    public UserDeclNode visitAssignDeclUser(DBLParser.AssignDeclUserContext ctx) {
        String typedef = ctx.typedefUser().getText();
        String expressionType = "";

        // This context has a number of children as its first, either this is an identifier or a bunch of template decle.
        // Find a way to loop through all of these template decl.

        UserDeclNode node = new UserDeclNode(typedef, expressionType);
        return node;
}
//TODO: 
    /*** Return ***/
    @Override
    public Return visitReturn(DBLParser.ReturnContext ctx) {
        if (!(ctx.expr() instanceof DigitExprContext) && ctx.expr() != null) {
            Expression expression = (Expression) visit(ctx.children.get(1));
            Return node = new Return(expression);
            System.out.println(node);
            return node;
        } else {
            int returnValue = Integer.valueOf(ctx.children.get(1).getText());
            Return node = new Return(returnValue);
            System.out.println(node);
            return node;
        }
    }

    /*** RuleDecl ***/
    @Override
    public RuleDecl visitRuleDecl(DBLParser.RuleDeclContext ctx) {
        // Add if else block
        RuleDecl node = new RuleDecl((If) visit(ctx.ifBlock()));

        // Add all actions
        for (ParseTree currentNode : ctx.identifierList().children) {
            node.getTriggerActions().add(currentNode.getText());
        }

        return node;
    }

    /*** Boolean Expressions ***/
    @Override
    public BoolExp visitExprGTBool(DBLParser.ExprGTBoolContext ctx) {
        BoolExp node = new BoolExp(BoolExpOperator.GREATER_THAN, null);
        node.addChild((AST) visit(ctx.children.get(0)));
        node.addChild((AST) visit(ctx.children.get(2)));
        return node;
    }

    @Override
    public BoolExp visitLitteralBool(DBLParser.LitteralBoolContext ctx) {
        BoolExp node = new BoolExp(BoolExpOperator.CONSTANT, Boolean.getBoolean(ctx.BOOLEAN().getText()));
        return node;
    }

    @Override
    public BoolExp visitAndBool(DBLParser.AndBoolContext ctx) {
        BoolExp node = new BoolExp(BoolExpOperator.AND, null);
        node.addChild((AST) visit(ctx.children.get(0)));
        node.addChild((AST) visit(ctx.children.get(2)));
        return node;
    }

    @Override
    public BoolExp visitExprGTOEBool(DBLParser.ExprGTOEBoolContext ctx) {
        BoolExp node = new BoolExp(BoolExpOperator.GREATER_OR_EQUALS, null);
        node.addChild((AST) visit(ctx.children.get(0)));
        node.addChild((AST) visit(ctx.children.get(2)));
        return node;
    }

    @Override
    public BoolExp visitStringEqualBool(DBLParser.StringEqualBoolContext ctx) {
        BoolExpOperator op = ctx.EQUALS() == null ? BoolExpOperator.NOT_EQUALS : BoolExpOperator.EQUALS;
        BoolExp node = new BoolExp(op, null);
        node.addChild((AST) visit(ctx.children.get(0)));
        node.addChild((AST) visit(ctx.children.get(2)));
        return node;
    }

    @Override
    public BoolExp visitExprLTOEBool(DBLParser.ExprLTOEBoolContext ctx) {
        BoolExp node = new BoolExp(BoolExpOperator.LESS_OR_EQUALS, null);
        node.addChild((AST) visit(ctx.children.get(0)));
        node.addChild((AST) visit(ctx.children.get(2)));
        return node;
    }

    @Override
    public Variable visitIdBool(DBLParser.IdBoolContext ctx) {
        Variable node = new Variable(ctx.IDENTIFIER().getText());
        return node;
    }

    @Override
    public BoolExp visitNegateBool(DBLParser.NegateBoolContext ctx) {
        BoolExp node = new BoolExp(BoolExpOperator.NOT, null);
        node.addChild((AST) visit(ctx.children.getLast()));
        return node;
    }

    @Override
    public BoolExp visitParBool(DBLParser.ParBoolContext ctx) {
        return (BoolExp) visit(ctx.children.get(1));
    }

    @Override
    public BoolExp visitExprLTBool(DBLParser.ExprLTBoolContext ctx) {
        BoolExp node = new BoolExp(BoolExpOperator.LESS_THAN, null);
        node.addChild((AST) visit(ctx.children.get(0)));
        node.addChild((AST) visit(ctx.children.get(2)));
        return node;
    }

    @Override
    public BoolExp visitOrBool(DBLParser.OrBoolContext ctx) {
        BoolExp node = new BoolExp(BoolExpOperator.OR, null);
        node.addChild((AST) visit(ctx.children.get(0)));
        node.addChild((AST) visit(ctx.children.get(2)));
        return node;
    }

    @Override
    public BoolExp visitEqualBool(DBLParser.EqualBoolContext ctx) {
        BoolExpOperator op = ctx.EQUALS() == null ? BoolExpOperator.NOT_EQUALS : BoolExpOperator.EQUALS;
        BoolExp node = new BoolExp(op, null);
        node.addChild((AST) visit(ctx.children.get(0)));
        node.addChild((AST) visit(ctx.children.get(2)));
        return node;
    }

    /*** For-loops ***/
    @Override
    public ForLoop visitForI(DBLParser.ForIContext ctx) {
        Identifiable iterator = (Identifiable) visit(ctx.children.get(2));
        BoolExp condition = (BoolExp) visit(ctx.children.get(3));
        Assignment iteratorAction = (Assignment) visit(ctx.children.get(5));
        Statement body = (Statement) visit(ctx.children.get(8));

        ForLoop node = new ForLoop(iterator, condition, iteratorAction, body);
        return node;
    }

    @Override
    public ForOf visitForOF(DBLParser.ForOFContext ctx) {
        ForOf node = new ForOf();
        UserDeclNode declNode = new UserDeclNode(ctx.children.get(3).getText(),
                                                ctx.children.get(2).getText());
        node.addChild(declNode);
        node.addChild((AST) visit(ctx.children.get(5)));
        node.addChild((AST) visit(ctx.children.get(8)));
        return node;
    }

    /*** If-statements ***/
    @Override
    public If visitIfBlock(DBLParser.IfBlockContext ctx) {
        int elseIfOffset = 7; // Offset from start to 'ELSE IF'  |  Begin search for ELSE IF-Statements at this index
        int boolExpOffSet = 2;
        int bodyOffset = 5;

        If node = new If();
        node.addChild((AST) visit(ctx.getChild(boolExpOffSet)));
        node.addChild((AST) visit(ctx.children.get(bodyOffset)));;

        List<TerminalNode> elseif = ctx.ELSEIF();
        for (int i = 1; i <= elseif.size(); i++){
            ElseIf eiNode = new ElseIf();
            eiNode.addChild((AST) visit(ctx.children.get(i * elseIfOffset + boolExpOffSet + 1))); // BoolExpr, +1 to align with next 'ELSE IF'
            eiNode.addChild((AST) visit(ctx.children.get(i * elseIfOffset + bodyOffset + 1)));    // Body, +1 to align with next 'ELSE IF'
        }

        if (ctx.ELSE() != null) {
            Else enode = new Else();
            enode.addChild((AST) visit(ctx.getChild(ctx.children.size()-1)));
        }
        return node;
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
        for (ParseTree treeNode : ctx.actionCall()) {
            node.addChild((AST) visit(treeNode));
        }
        return node;
    }

    /*** String expression ***/
    @Override
    public StringExp visitAddString(DBLParser.AddStringContext ctx) {
        StringExp node = new StringExp(StringExpOperator.CONCAT, null);
        node.addChild((AST) visit(ctx.stringExpr(0)));
        node.addChild((AST) visit(ctx.stringExpr(1)));
        return node;
    }

    @Override
    public StringExp visitAddStringexpr1(DBLParser.AddStringexpr1Context ctx) {
        StringExp node = new StringExp(StringExpOperator.CONCAT, null);
        node.addChild((AST) visit(ctx.stringExpr()));
        node.addChild((AST) visit(ctx.expr()));
        return node;
    }

    @Override
    public StringExp visitAddStringexpr2(DBLParser.AddStringexpr2Context ctx) {
        StringExp node = new StringExp(StringExpOperator.CONCAT, null);
        node.addChild((AST) visit(ctx.expr()));
        node.addChild((AST) visit(ctx.stringExpr()));
        return node;
    }

    @Override
    public StringExp visitLitteralString(DBLParser.LitteralStringContext ctx) {
        StringExp node = new StringExp(StringExpOperator.LITERAL, ctx.string().getText());
        return node;
    }

    @Override
    public Variable visitIdString(DBLParser.IdStringContext ctx) {
        Variable node = new Variable(ctx.IDENTIFIER().getText());
        return node;
    }

    /*** Templates ***/
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
        for (ParseTree treeNode : ctx.children) {
            if (treeNode instanceof DBLParser.TemplateInitContext || treeNode instanceof DBLParser.AssignmentContext) {
                node.addChild((AST) visit(ctx));
            }
        }
        return node;
    }

    @Override
    public TemplateAccess visitTemplateAccess(DBLParser.TemplateAccessContext ctx) {
        TemplateAccess node = new TemplateAccess(ctx.typedefUser().getText(), (Variable) visit(ctx.IDENTIFIER(0)));
        for (int i = 1; i < ctx.IDENTIFIER().size(); i++) {
            node.addChild((AST) visit(ctx.IDENTIFIER(i)));
        }
        return node;
    }

    /**
     * Visit a parse tree produced by {@link DBLParser#actionCall}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
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
