package com.proj4;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.proj4.AST.nodes.*;
import com.proj4.antlrClass.DBLBaseVisitor;
import com.proj4.antlrClass.DBLParser;
import com.proj4.symbolTable.symbols.BooleanSymbol;
import com.proj4.symbolTable.symbols.IntegerSymbol;
import com.proj4.symbolTable.symbols.StringSymbol;


public class ParseTreeVisitor extends DBLBaseVisitor<Object> {
    // Fields
    private AST root;
    private Boolean debugMode = false;

    // Methods
    public AST getRoot() {
        return this.root;
    }

    public void setDebugMode(Boolean debugMode) {
        this.debugMode = debugMode;
    }

    /*
     * ┌─────────────────────────────────┐
     * │     Set up Root Node of AST     │
     * └─────────────────────────────────┘
     */
    @Override
    public AST visitProgram(DBLParser.ProgramContext ctx) {
        this.root = new Program();
        try {
            // count is subtracted by one to ignore EOF node in parse tree
            for (int i = 0; i < ctx.getChildCount() - 1; i++) {
                var childnode = visit(ctx.getChild(i));
                this.root.addChild((AST) childnode);
            }
        } catch (Exception e) {
            if(debugMode){
                System.out.println(this.getClass().getSimpleName() + " exploded. Too bad :(\n");
            }
            throw e;
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

    /*** Generals ***/
    @Override
    public Body visitBody(DBLParser.BodyContext ctx) {
        return (Body) visit(ctx.stmtList());
    }

    @Override
    public ArrayList<Expression> visitArgumentList(DBLParser.ArgumentListContext ctx){
        ArrayList<Expression> argumentNodes = new ArrayList<Expression>();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            Expression node = (Expression)visit(ctx.getChild(i));
            if (node != null) {
                argumentNodes.add(node);
            }
        }
        return argumentNodes;
    }

    @Override
    public ArrayList<Declaration> visitParameterList(DBLParser.ParameterListContext ctx) {
        ArrayList<Declaration> parameterNodes = new ArrayList<Declaration>();
        ParseTree childNode = null;
        String type = null;
        String complexType = null;
        Integer nestingLevel = -1;
        //modulo expressions here separate parameters as each parameter is in the form: Type - Identifier - ,
        for (int i = 0; i < ctx.getChildCount(); i++){
            if((i)%3 == 0){ // Get type
                childNode = ctx.getChild(i);
                if (childNode.getClass().getSimpleName().equals("TypedefUserContext")) {
                    type = childNode.getText();
                    complexType = "Template";
                }
                else if(childNode.getText().contains("[")) {
                    type = childNode.getText().replaceAll("\\[\\]", "");
                    complexType = "Array";
                    nestingLevel = (int) childNode.getText().chars().filter(ch -> ch == '[').count()-1; // Count number of "[" to find nestinglevel
                }
                else {
                    type = childNode.getText();
                    complexType = "Primitive";
                }
            }
            if((i)%3 == 1){ // Get identifier of type
                String identifier = ctx.getChild(i).getText();
                Declaration node = nestingLevel > -1 ? new Declaration(identifier, type, complexType, nestingLevel) : new Declaration(identifier, type, complexType);
                parameterNodes.add(node);
            }
        }
        return parameterNodes;
    }

    @Override
    public ArrayList<String> visitIdentifierList(DBLParser.IdentifierListContext ctx) {
        ArrayList<String> identifiers = new ArrayList<String>();
        for (TerminalNode node : ctx.IDENTIFIER()){
            identifiers.add(node.getText());
        }
        return identifiers;
    }


    /*** Actions ***/
    @Override
    public ActionDecl visitReturnActionDecl(DBLParser.ReturnActionDeclContext ctx) {
        ArrayList<String> resultTypes = (ArrayList<String>) visit(ctx.resultsIn());
        String returnType = resultTypes.get(0);
        String complexReturnType = resultTypes.get(1);
        Integer returnNestingLevel = -1;

        if(resultTypes.get(2) != null){
            returnNestingLevel = Integer.valueOf(resultTypes.get(2));
        }

        Body body = (Body) visit(ctx.body());
        body.addChild((Return) visit(ctx.return_()));
        String identifier = ctx.typedefUser().getText();
        ActionDecl node = new ActionDecl(identifier, returnType, complexReturnType, body, returnNestingLevel);

        ArrayList<Declaration> parameterList = (ArrayList<Declaration>) visit(ctx.parameterList());
        for (Declaration parameter : (ArrayList<Declaration>) parameterList) {
            node.addChild(parameter);
        }
        return node;
    }

    @Override
    public ActionDecl visitNoReturnActionDecl(DBLParser.NoReturnActionDeclContext ctx){
        String identifier = ctx.typedefUser().getText();
        ActionDecl node = new ActionDecl(identifier, (Body) visit(ctx.body()));
        ArrayList<Declaration> parameterList = (ArrayList<Declaration>) visit(ctx.parameterList());
        for (Declaration parameter : (ArrayList<Declaration>) parameterList) {
            node.addChild((AST) parameter);
        }
        return node;
    }

    @Override
    public Expression visitActionResult(DBLParser.ActionResultContext ctx){
        Expression actionResultNode = new Expression(ExpressionOperator.ACCESS, new Variable(ctx.typedefUser().getText()), new TField(ctx.RESULT().getText()));
        Expression accessNode = null;
        Integer size = ctx.IDENTIFIER().size();
        for(int i = 0; i < size; i++){
            if(i == 0){
                accessNode = new Expression(ExpressionOperator.ACCESS, actionResultNode, new TField(ctx.IDENTIFIER().get(i).getText()));
                System.out.println("First: " + ctx.IDENTIFIER().get(i).getText());
            } else {
                accessNode = new Expression(ExpressionOperator.ACCESS, accessNode, new TField(ctx.IDENTIFIER().get(i).getText()));
                System.out.println("Subseq " + i + " : " + ctx.IDENTIFIER().get(i).getText());
            }
        }
        return accessNode != null ? accessNode : actionResultNode;
    }

    @Override
    public ArrayList<String> visitResultsIn(DBLParser.ResultsInContext ctx) {
        String complexType = null;
        String type = null;
        String nestingLevel = null;
        if(ctx.typedefUser() != null) {
            type = ctx.typedefUser().getText();
            complexType = "Template";
        } else if(ctx.typePrimitive() != null) {
            type = ctx.typePrimitive().getText();
            complexType = "Primitive";
        } else if(ctx.arrayType() != null) {
            nestingLevel = "" + (ctx.arrayType().getText().chars().filter(ch -> ch == '[').count()-1);
            type = ctx.arrayType().getText().replaceAll("\\[\\]", "");
            complexType = "Array";
        }
        ArrayList<String> resultTypes = new ArrayList<String>();
        resultTypes.add(type);
        resultTypes.add(complexType);
        resultTypes.add(nestingLevel);
        return resultTypes;
    }

    @Override
    public ActionCall visitActionCall(DBLParser.ActionCallContext ctx) {
        ActionCall node = new ActionCall(ctx.typedefUser().getText());

        ArrayList<Expression> arguments = (ArrayList<Expression>) visit(ctx.argumentList());
        for (Expression argument : arguments) {
            node.addChild(argument);
        }
        return node;
    }

    @Override
    public Return visitReturn(DBLParser.ReturnContext ctx) {
        Return node = new Return((Expression) visit(ctx.children.get(1)));
        return node;
    }


    /*** ASSIGNMENT ***/
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


    /*** Arrays ***/
    @Override
    public ArrayList<String> visitArrayType(DBLParser.ArrayTypeContext ctx) {
        String type = null;
        if(ctx.typePrimitive() != null) {
            type = ctx.typePrimitive().getText();
        }
        if(ctx.typedefUser() != null) {
            type = ctx.typedefUser().getText();
        }
        String nestingLevel = String.format("%d", ctx.SQB_START().size()-1);
        ArrayList<String> arr = new ArrayList<String>();
        arr.add(type);
        arr.add(nestingLevel);
        return arr;
    }

    @Override
    public Declaration visitDeclArrayInit(DBLParser.DeclArrayInitContext ctx) {
        ArrayList<String> arr = (ArrayList<String>) visit(ctx.arrayType());
        String arrayType = arr.get(0);
        Integer nestingLevel = Integer.valueOf(arr.get(1)); // Get nesting level from declaration

        Assignment assignNode = (Assignment) visit(ctx.assignment());
        Variable variableNode = (Variable) assignNode.getSymbolExpression(); // Get left side of assign

        Declaration node = new Declaration(variableNode.getIdentifier(), arrayType, "Array", nestingLevel);
        node.addChild(assignNode);
        return node;
    }

    @Override
    public Declaration visitDeclArrayDecl(DBLParser.DeclArrayDeclContext ctx) {
        ArrayList<String> arr = (ArrayList<String>) visit(ctx.arrayType());
        String arrayType = arr.get(0);
        Integer nestingLevel = Integer.valueOf(arr.get(1)); // Get nesting level from declaration
        String identifier = ctx.IDENTIFIER().getText();

        Declaration node = new Declaration(identifier, arrayType, "Array", nestingLevel);
        return node;
    }


    /*** Expressions ***/
    @Override
    public TemplateInstance visitTemplateInitExpr(DBLParser.TemplateInitExprContext ctx) {
        TemplateInstance node = (TemplateInstance)visit(ctx.templateInit());
        return node;
    }

    @Override public Expression visitTemplateAccessExpr(DBLParser.TemplateAccessExprContext ctx) {
        Expression node = new Expression(ExpressionOperator.ACCESS, (Expression) visit(ctx.expr()), new TField(ctx.IDENTIFIER().getText()));
        return node;
    }

    @Override
    public ActionCall visitActionCallExpr(DBLParser.ActionCallExprContext ctx) {
        ActionCall node = (ActionCall) visit(ctx.actionCall());
        return node;
    }

    @Override
    public Expression visitActionResultExpr(DBLParser.ActionResultExprContext ctx) {
        Expression node = (Expression) visit(ctx.actionResult());
        return node;
    }

    @Override
    public Expression visitArrayInitExpr(DBLParser.ArrayInitExprContext ctx) {
        return (Expression) visit(ctx.arrayInit());
    }

    @Override
    public Expression visitArrayAccessExpr(DBLParser.ArrayAccessExprContext ctx) {
        Expression node = new Expression(ExpressionOperator.INDEX, (Expression) visit(ctx.expr(0)), (Expression) visit(ctx.expr(1)));
        return node;
    }

    @Override
    public ArrayInstance visitArrayInit(DBLParser.ArrayInitContext ctx) {
        ArrayInstance node = new ArrayInstance();
        for (int i = 0; i < ctx.getChildCount(); i++){
            Expression childnode = (Expression) visit(ctx.getChild(i));
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
        ExpressionOperator op = ctx.multOp().MULT() == null ? ExpressionOperator.DIVIDE : ExpressionOperator.MULTIPLY;
        Expression node = new Expression(op, (Expression) visit(ctx.expr(0)),(Expression)visit(ctx.expr(1)));
        return node;
    }

    @Override
    public Expression visitAddExpr(DBLParser.AddExprContext ctx) {
        ExpressionOperator op = ctx.addOp().ADD() == null ? ExpressionOperator.SUBTRACT : ExpressionOperator.ADD;
        Expression node = new Expression(op, (Expression) visit(ctx.expr(0)), (Expression) visit(ctx.expr(1)));
        return node;
    }

    @Override
    public Expression visitDigitExpr(DBLParser.DigitExprContext ctx) {
        Expression node = new Expression(ExpressionOperator.CONSTANT, new IntegerSymbol(Integer.parseInt(ctx.DIGIT().getText())));
        return node;
    }

    @Override
    public Variable visitIdExpr(DBLParser.IdExprContext ctx) {
        Variable node = new Variable(ctx.IDENTIFIER().getText());
        return node;
    }


    /*** Declaration ***/
    @Override
    public Declaration visitIdDeclPrim(DBLParser.IdDeclPrimContext ctx) {
        Declaration node = new Declaration(ctx.IDENTIFIER().getText(),ctx.typePrimitive().getText(), "Primitive");
        return node;
    }

    @Override public Declaration visitAssignDeclPrim(DBLParser.AssignDeclPrimContext ctx) {
        Assignment assignNode = (Assignment) visit(ctx.assignment());
        Variable variableNode = (Variable) assignNode.getSymbolExpression(); // Get left side of assign

        String typePrim = ctx.typePrimitive().getText();
        Declaration node = new Declaration(variableNode.getIdentifier(), typePrim, "Primitive");
        node.addChild(assignNode);
        return node;
    }

    @Override
    public Declaration visitIdDeclUser(DBLParser.IdDeclUserContext ctx) {
        Declaration node = new Declaration(ctx.IDENTIFIER().getText(), ctx.typedefUser().getText(), "Template");
        return node;
    }

    @Override
    public Declaration visitAssignIdUserDecl(DBLParser.AssignIdUserDeclContext ctx){
        Assignment assignNode = (Assignment) visit(ctx.assignment());
        Variable variableNode = (Variable) assignNode.getSymbolExpression(); // Get left side of assign
        Declaration node = new Declaration(variableNode.getIdentifier(), ctx.typedefUser().getText(), "Template");

        node.addChild(assignNode);
        return node;
    }


    /*** Rules ***/
    @Override
    public RuleDecl visitRuleDecl(DBLParser.RuleDeclContext ctx) {
        // Add if else block
        RuleDecl node = new RuleDecl((IfElse) visit(ctx.ifBlock()));

        // Add all actions
        for (String actionID : (ArrayList<String>) visit(ctx.identifierList())) {
            node.getTriggerActions().add(actionID);
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
        Expression node = new Expression(ExpressionOperator.CONSTANT, new BooleanSymbol(Boolean.parseBoolean(ctx.BOOLEAN().getText())));
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
    public Expression visitExprEqualBool(DBLParser.ExprEqualBoolContext ctx) {
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

    @Override
    public ActionCall visitActionCallBool(DBLParser.ActionCallBoolContext ctx){
        ActionCall node = (ActionCall) visit(ctx.actionCall());
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


    /*** States ***/
    @Override
    public StateDecl visitIdActionStateLoop(DBLParser.IdActionStateLoopContext ctx) {
        StateDecl node = new StateDecl(ctx.typedefUser().getText(), (ArrayList<String>) visit(ctx.identifierList()), (Body) visit(ctx.body()));
        return node;
    }

    @Override
    public StateDecl visitIdActionState(DBLParser.IdActionStateContext ctx) {
        StateDecl node = new StateDecl(ctx.typedefUser().getText(), (ArrayList<String>) visit(ctx.identifierList()));
        return node;
    }


    /*** String expressions ***/
    @Override
    public Expression visitAddString(DBLParser.AddStringContext ctx) {
        Expression node = new Expression(ExpressionOperator.ADD, (Expression) visit(ctx.stringExpr(0)), (Expression) visit(ctx.stringExpr(1)));
        return node;
    }

    @Override
    public Expression visitAddStringexpr1(DBLParser.AddStringexpr1Context ctx) {
        Expression node = new Expression(ExpressionOperator.ADD, (Expression) visit(ctx.stringExpr()), (Expression) visit(ctx.expr()));
        return node;
    }

    @Override
    public Expression visitAddStringexpr2(DBLParser.AddStringexpr2Context ctx) {
        Expression node = new Expression(ExpressionOperator.ADD, (Expression) visit(ctx.expr()), (Expression) visit(ctx.stringExpr()));
        return node;
    }

    @Override
    public Expression visitLitteralString(DBLParser.LitteralStringContext ctx) {
        String str = ctx.string().getText();
        Expression node = new Expression(ExpressionOperator.CONSTANT, new StringSymbol(str.substring(1, str.length()-1)));
        return node;
    }

    @Override
    public Variable visitIdString(DBLParser.IdStringContext ctx) {
        Variable node = new Variable(ctx.IDENTIFIER().getText());
        return node;
    }

    // @Override
    // public Expression visitActionResultString(DBLParser.ActionResultStringContext ctx) {
    //     Expression node = (Expression) visit(ctx.actionResult());
    //     return node;
    // }

    @Override
    public ActionCall visitActionCallString(DBLParser.ActionCallStringContext ctx){
        ActionCall node = (ActionCall) visit(ctx.actionCall());
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
            if (treeNode instanceof DBLParser.TemplateInitContext || treeNode instanceof DBLParser.ExprContext || treeNode instanceof DBLParser.StringExprContext || treeNode instanceof DBLParser.BoolExprContext) {
                node.addChild((AST) visit(treeNode));
            }
        }
        return node;
    }
}