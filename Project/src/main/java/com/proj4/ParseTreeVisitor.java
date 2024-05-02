package com.proj4;

import com.proj4.antlrClass.DBLBaseVisitor;
import com.proj4.antlrClass.DBLParser;
import com.proj4.antlrClass.DBLParser.ArrayTypeContext;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

import com.proj4.AST.nodes.*;
import com.proj4.symbolTable.symbols.BooleanSymbol;
import com.proj4.symbolTable.symbols.IntSymbol;
import com.proj4.symbolTable.symbols.StringSymbol;


public class ParseTreeVisitor extends DBLBaseVisitor<Object> {
    Boolean DEBUG_MODE = false;  // USE THIS WHEN DEBUGGING!!!

    // Fields
    private AST root;
    public AST getRoot() {
        return this.root;
    }
    /*
     * ┌─────────────────────────────────┐
     * │     Set up Root Node of AST     │
     * └─────────────────────────────────┘
     */

    @Override
    public Object visitProgram(DBLParser.ProgramContext ctx) {
        this.root = new Program();
        try {
            // count is subtracted by one to ignore EOF node in parse tree
            for (int i = 0; i < ctx.getChildCount() - 1; i++) {
                var childnode = visit(ctx.getChild(i));
                this.root.addChild((AST) childnode);
            }
            System.out.println("Program Children: " + root.getChildren());
            return root;
        } catch (Exception e) {
            if(DEBUG_MODE){
                System.out.println("Something exploded. Too bad :(\n");
                e.printStackTrace();
            } else {
                System.out.println("Syntax error in input!");
            }
        }
        return this.root;
    }

    @Override
    public Body visitStmtList(DBLParser.StmtListContext ctx) {
        Body node = new Body();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            var childnode = visit(ctx.getChild(i));
            if (childnode != null) {
                node.addChild((AST) childnode);
            }
        }
        return node;
    }

    @Override
    public AST visitStmt(DBLParser.StmtContext ctx) {
        AST childnode = (AST) visit(ctx.getChild(0));
        return childnode;
    }

    /*
     * ┌────────────────────────────────────────────────────────────┐
     * │    Override Visitor implementations provided by ANTLR      │
     * └────────────────────────────────────────────────────────────┘
     */
    @Override
    public Declaration visitIdDeclPrim(DBLParser.IdDeclPrimContext ctx) {
        Declaration node = new Declaration(ctx.IDENTIFIER().getText(),ctx.typePrimitive().getText(), "Primitive");
        return node;

    }

    /*** ACTION DECLARATION ***/

    @Override
    public ActionDecl visitReturnActionDecl(DBLParser.ReturnActionDeclContext ctx) {
        ParseTree resultsIn = ctx.resultsIn();
        String resultComplexType;
        String resultType = resultsIn.getText();
        Integer nestingLevel = 0;
        if (resultsIn.getClass().getSimpleName().equals("TypedefUserContext")) {
            resultComplexType = "Template";
        }
        else if(resultsIn.getText().contains("[")) {
            resultComplexType = "Array";
            nestingLevel = (int) resultsIn.getText().chars().filter(ch -> ch == '[').count()-1; // Count number of "[" to find nestinglevel
        } 
        else {
            resultComplexType = "Primitive";
        }
        String identifier = ctx.typedefUser().getText();
        ActionDecl node = new ActionDecl(identifier, resultType, resultComplexType, (Body) visit(ctx.body()), Math.max(0, nestingLevel));

        ArrayList<Declaration> parameterList = (ArrayList<Declaration>) visit(ctx.parameterList());
        for (Declaration parameter : (ArrayList<Declaration>) parameterList) {
            node.addChild((AST) parameter);
        }
        return node;
    }

    @Override
    public ActionDecl visitNoReturnActionDecl(DBLParser.NoReturnActionDeclContext ctx){
        String identifier = ctx.getChild(1).getText();
        ActionDecl node = new ActionDecl(identifier, (Body) visit(ctx.body()));
        ArrayList<Declaration> parameterList = (ArrayList<Declaration>) visit(ctx.parameterList());
        for (Declaration parameter : (ArrayList<Declaration>) parameterList) {
            node.addChild((AST) parameter);
        }
        return node;
    }

    @Override
    public ArrayList<Declaration> visitParameterList(DBLParser.ParameterListContext ctx) {
        ArrayList<Declaration> parameterNodes = new ArrayList<Declaration>();
        ParseTree type = null;
        String complexType = null;
        Integer nestingLevel = -1;
        //modulo expressions here separate parameters as each parameter is in the form: Type - Identifier - ,
        for (int i = 0; i < ctx.getChildCount(); i++){
            if((i)%3 == 0){ // Get type
                type = ctx.getChild(i);
                if (type.getClass().getSimpleName().equals("TypedefUserContext")) {
                    complexType = "Template";
                }
                else if(type.getText().contains("[")) {
                    complexType = "Array";
                    nestingLevel = (int) type.getText().chars().filter(ch -> ch == '[').count()-1; // Count number of "[" to find nestinglevel
                } 
                else {
                    complexType = "Primitive";
                }
            }
            if((i)%3 == 1){ // Get identifier of type
                String identifier = ctx.getChild(i).getText();
                Declaration node = nestingLevel > -1 ? new Declaration(identifier, type.getText(),complexType, Math.max(0, nestingLevel)) : new Declaration(identifier, type.getText(),complexType);
                parameterNodes.add(node);
            }
        }
        return parameterNodes;
    }

    /*** ACTION CALL ***/

    @Override
    public ActionCall visitActionCall(DBLParser.ActionCallContext ctx) {
        ActionCall node = new ActionCall(ctx.typedefUser().getText());

        for (int i = 0; i < ctx.getChildCount(); i++) {
            var childnode = visit(ctx.getChild(i));
            if (childnode != null){
                node.addChild((Expression) childnode);
            }
        }
        return node;
    }

    @Override
    public ActionCall visitActionCallExpr(DBLParser.ActionCallExprContext ctx) {
        ActionCall node = (ActionCall) visit(ctx.actionCall());
        return node;
    }

    /*** ASSIGNMENT ***/
    @Override
    public Assignment visitIdAssign(DBLParser.IdAssignContext ctx) {
        Assignment node = new Assignment((Expression) visit(ctx.expr()), new Variable(ctx.IDENTIFIER().getText()));
        return node;
    }

    @Override
    public Assignment visitExprAssign(DBLParser.ExprAssignContext ctx) {
        Assignment node = new Assignment((Expression) visit(ctx.expr(0)), (Expression) visit(ctx.expr(1)));
        return node;
    }

    @Override
    public Assignment visitBoolExprAssign(DBLParser.BoolExprAssignContext ctx) {
        Assignment node = new Assignment((Expression) visit(ctx.expr()), (Expression) visit(ctx.boolExpr()));
        return node;
    }

    @Override
    public Assignment visitStringExprAssign(DBLParser.StringExprAssignContext ctx) {
        Assignment node = new Assignment((Expression) visit(ctx.expr()), (Expression) visit(ctx.stringExpr()));
        return node;
    }

    @Override
    public Assignment visitArrayInitAssign(DBLParser.ArrayInitAssignContext ctx) {
        Assignment node = new Assignment((Expression) visit(ctx.expr()), (Expression) visit(ctx.arrayInit()));
        return node;
    }

    @Override
    public Assignment visitTemplateAssignment(DBLParser.TemplateAssignmentContext ctx) {
        Assignment node = new Assignment(new Variable(ctx.typedefUser().getText()), (Expression) visit(ctx.templateInit()));
        return node;
    }

    /*** ARRAY DECLARATION ***/
    @Override
    /**
     * Declaration of an array - can happen everywhere
     */
    public Declaration visitArrayDecl(DBLParser.ArrayDeclContext ctx) {
        String valueType = null;
        if(ctx.typePrimitive() != null) {
            valueType = ctx.typePrimitive().getText();
        }
        if(ctx.typedefUser() != null) {
            valueType = ctx.typedefUser().getText();
        }
        String identifier = ctx.getChild(3).getText();
        Declaration node = new Declaration(identifier, valueType, "Array");
        if(ctx.arrayInit() != null){
            node.addChild((ArrayInstance) visit(ctx.arrayInit()));
        }
        return node;
    }

    @Override
    /**
     * Declaration of an array - this instance is declared alone, i.e. on a line for itself
     */
    public Declaration visitDeclArrayDecl(DBLParser.DeclArrayDeclContext ctx) {
        return (Declaration) visit(ctx.arrayDecl());
    }

    /*** ARRAY Access ***/
    @Override
    public Expression visitArrayAccessExpr(DBLParser.ArrayAccessExprContext ctx) {
        Expression node = new Expression(ExpressionOperator.INDEX, (Expression) visit(ctx.expr(0)), (Expression) visit(ctx.expr(1)));
        return node;
    }

    @Override
    public ArrayInstance visitArrayInit(DBLParser.ArrayInitContext ctx) {
        ArrayInstance node = new ArrayInstance();
        for (int i = 0; i < ctx.getChildCount(); i++){
            var childnode = (Expression) visit(ctx.getChild(i));
            if (childnode != null) {
                node.addChild(childnode);
            }
        }
        return node;
    }

    @Override
    public Expression visitParExpr(DBLParser.ParExprContext ctx) {
        return (Expression) visit(ctx.expr());
    }

    @Override
    public Expression visitMultExpr(DBLParser.MultExprContext ctx) {
        Expression node = new Expression(ExpressionOperator.MULTIPLY, (Expression) visit(ctx.expr(0)),(Expression)visit(ctx.expr(1)));
        return node;
    }

    @Override
    public Expression visitAddExpr(DBLParser.AddExprContext ctx) {
        Expression node = new Expression(ExpressionOperator.ADD, (Expression) visit(ctx.expr(0)), (Expression) visit(ctx.expr(1)));
        return node;
    }

    @Override
    public Expression visitDigitExpr(DBLParser.DigitExprContext ctx) {
        Expression node = new Expression(ExpressionOperator.CONSTANT, new IntSymbol(Integer.parseInt(ctx.DIGIT().getText())));
        return node;
    }

    @Override
    public Variable visitIdExpr(DBLParser.IdExprContext ctx) {
        Variable node = new Variable(ctx.IDENTIFIER().getText());
        return node;
    }

    /*** Declaration ***/
    @Override public Declaration visitAssignDeclPrim(DBLParser.AssignDeclPrimContext ctx) {
        String typedef = ctx.typePrimitive().getText();
        String identifier = null;
        Declaration node = new Declaration(identifier, typedef, "Primitive");
        node.addChild((Assignment) visit(ctx.assignment()));
        return node;
    }

    @Override
    public TemplateDecl visitIdDeclUser(DBLParser.IdDeclUserContext ctx) {
        TemplateDecl node = new TemplateDecl(ctx.IDENTIFIER().getText());
        return node;
    }

    @Override
    public TemplateDecl visitAssignDeclUser(DBLParser.AssignDeclUserContext ctx) {
        String typedef = ctx.typedefUser().getText();
        Assignment assignment = (Assignment) visit(ctx.assignment());

        TemplateDecl node = new TemplateDecl(typedef);
        node.addChild(assignment);
        return node;
    }
    /*** Return ***/
    @Override
    public Return visitReturn(DBLParser.ReturnContext ctx) {
        Return node = new Return((Expression) visit(ctx.children.get(1)));
        return node;
    }

    /*** ResultsIn ***/
    // @Override
    // public Object visitNormalResultsIn(DBLParser.NormalResultsInContext ctx) {
    //     return ctx.getChild(1).getText();
    // }

    // @Override
    // public Object visitArrayPrimResultsIn(DBLParser.ArrayPrimResultsInContext ctx) {
    //     ArrayList<String> output = new ArrayList<String>();
    //     output.add(ctx.typePrimitive().getText());
    //     output.add(Integer.toString(ctx.SQB_START().size())) ? ctx.SQB_START().size() > 0 : output.add(null);

    // }

    // @Override
    // public Object visitArrayUserResultsIn(DBLParser.ArrayUserResultsInContext ctx) {
        
    // }


    /*** RuleDecl ***/
    @Override
    public RuleDecl visitRuleDecl(DBLParser.RuleDeclContext ctx) {
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
    public Expression visitExprGTBool(DBLParser.ExprGTBoolContext ctx) {
        Expression node = new Expression(ExpressionOperator.GREATER_THAN, (Expression) visit(ctx.children.get(0)), (Expression) visit(ctx.children.get(2)));
        return node;
    }

    @Override
    public Expression visitLitteralBool(DBLParser.LitteralBoolContext ctx) {
        Expression node = new Expression(ExpressionOperator.CONSTANT, new BooleanSymbol(Boolean.getBoolean(ctx.BOOLEAN().getText())));
        return node;
    }

    @Override
    public Expression visitAndBool(DBLParser.AndBoolContext ctx) {
        Expression node = new Expression(ExpressionOperator.AND, (Expression) visit(ctx.children.get(0)), (Expression) visit(ctx.children.get(2)));
        return node;
    }

    @Override
    public Expression visitExprGTOEBool(DBLParser.ExprGTOEBoolContext ctx) {
        Expression node = new Expression(ExpressionOperator.GREATER_OR_EQUALS, (Expression) visit(ctx.children.get(0)), (Expression) visit(ctx.children.get(2)));
        return node;
    }

    @Override
    public Expression visitStringEqualBool(DBLParser.StringEqualBoolContext ctx) {
        ExpressionOperator op = ctx.EQUALS() == null ? ExpressionOperator.NOT_EQUALS : ExpressionOperator.EQUALS;
        Expression node = new Expression(op, (Expression) visit(ctx.children.get(0)), (Expression) visit(ctx.children.get(2)));
        return node;
    }

    @Override
    public Expression visitExprLTOEBool(DBLParser.ExprLTOEBoolContext ctx) {
        Expression node = new Expression(ExpressionOperator.LESS_OR_EQUALS, (Expression) visit(ctx.children.get(0)), (Expression) visit(ctx.children.get(2)));
        return node;
    }

    @Override
    public Variable visitIdBool(DBLParser.IdBoolContext ctx) {
        Variable node = new Variable(ctx.IDENTIFIER().getText());
        return node;
    }

    @Override
    public Expression visitNegateBool(DBLParser.NegateBoolContext ctx) {
        Expression node = new Expression(ExpressionOperator.NOT, (Expression) visit(ctx.children.getLast()));
        return node;
    }

    @Override
    public Expression visitParBool(DBLParser.ParBoolContext ctx) {
        return (Expression) visit(ctx.children.get(1));
    }

    @Override
    public Expression visitExprLTBool(DBLParser.ExprLTBoolContext ctx) {
        Expression node = new Expression(ExpressionOperator.LESS_THAN, (Expression) visit(ctx.children.get(0)), (Expression) visit(ctx.children.get(2)));
        return node;
    }

    @Override
    public Expression visitOrBool(DBLParser.OrBoolContext ctx) {
        Expression node = new Expression(ExpressionOperator.OR, (Expression) visit(ctx.children.get(0)), (Expression) visit(ctx.children.get(2)));
        return node;
    }

    @Override
    public Expression visitEqualBool(DBLParser.EqualBoolContext ctx) {
        ExpressionOperator op = ctx.EQUALS() == null ? ExpressionOperator.NOT_EQUALS : ExpressionOperator.EQUALS;
        Expression node = new Expression(op, (Expression) visit(ctx.children.get(0)), (Expression) visit(ctx.children.get(2)));
        return node;
    }

    /*** For-loops ***/
    @Override
    public ForLoop visitForI(DBLParser.ForIContext ctx) {
        Declaration iterator = (Declaration) visit(ctx.declaration());
        Expression condition = (Expression) visit(ctx.boolExpr());
        Assignment iteratorAction = (Assignment) visit(ctx.assignment());
        Body body = (Body) visit(ctx.body());

        ForLoop node = new ForLoop(iterator, condition, iteratorAction, body);
        return node;
    }

    /*** If-statements ***/
    @Override
    public IfElse visitIfBlock(DBLParser.IfBlockContext ctx) {
        IfElse node = new IfElse();
        node.addChild((Expression) visit(ctx.boolExpr(0)));
        node.addChild((Body) visit(ctx.body(0)));

        IfElse tempNode = node;
        List<TerminalNode> elseif = ctx.ELSEIF();
        for (int i = 1; i <= elseif.size(); i++){
            IfElse eiNode = new IfElse();
            eiNode.addChild((Expression) visit(ctx.boolExpr(i)));
            eiNode.addChild((Body) visit(ctx.body(i)));

            tempNode.addChild(eiNode);
            tempNode = eiNode;
        }

        if (ctx.ELSE() != null) {
            tempNode.addChild((Body) visit(ctx.body().getLast()));
        }
        return node;
    }

    @Override
    public Body visitBody(DBLParser.BodyContext ctx) {
        return (Body) visit(ctx.stmtList());
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
            node.addChild((ActionCall) visit(treeNode));
        }
        return node;
    }

    /*** String expression ***/
    @Override
    public Expression visitAddString(DBLParser.AddStringContext ctx) {
        Expression node = new Expression(ExpressionOperator.CONCAT, (Expression) visit(ctx.stringExpr(0)), (Expression) visit(ctx.stringExpr(1)));
        return node;
    }

    @Override
    public Expression visitAddStringexpr1(DBLParser.AddStringexpr1Context ctx) {
        Expression node = new Expression(ExpressionOperator.CONCAT, (Expression) visit(ctx.stringExpr()), (Expression) visit(ctx.expr()));
        return node;
    }

    @Override
    public Expression visitAddStringexpr2(DBLParser.AddStringexpr2Context ctx) {
        Expression node = new Expression(ExpressionOperator.CONCAT, (Expression) visit(ctx.expr()), (Expression) visit(ctx.stringExpr()));
        return node;
    }

    @Override
    public Expression visitLitteralString(DBLParser.LitteralStringContext ctx) {
        Expression node = new Expression(ExpressionOperator.CONSTANT, new StringSymbol(ctx.string().getText()));
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
        TemplateInstance node = new TemplateInstance(ctx.typedefUser().getText());
        for (ParseTree treeNode : ctx.children) {
            if (treeNode instanceof DBLParser.TemplateInitContext || treeNode instanceof DBLParser.AssignmentContext) {
                node.addChild((AST) visit(treeNode));
            }
        }
        return node;
    }

    @Override public Expression visitTemplateAccessExpr(DBLParser.TemplateAccessExprContext ctx) {
        Expression node = new Expression(ExpressionOperator.ACCESS, (Expression) visit(ctx.expr()), new TField(ctx.IDENTIFIER().getText()));
        return node;
    }
}