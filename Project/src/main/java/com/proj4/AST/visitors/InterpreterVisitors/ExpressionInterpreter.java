package com.proj4.AST.visitors.InterpreterVisitors;

import java.util.ArrayList;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Expression;
import com.proj4.AST.nodes.TField;
import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.exceptions.*;
import com.proj4.symbolTable.Scope;
import com.proj4.symbolTable.symbols.*;

public class ExpressionInterpreter extends InterpreterVisitor {

    @SuppressWarnings("unchecked")  //typecasting without respecting generics is ok here, because we've already typechecked the program at this point
    public void visit(AST node) {
        // Expression: primitive symbol + expression operator left operand, right operand. 
        Expression expression = (Expression) node;
        Integer integerResult;
        Boolean booleanResult;
        Integer operands[];
        Boolean booleanOperands[];
        // Note: These operators always return primitive types
        // Note: Fallthrough for each operator type
        switch (expression.getOperator()) {
            case ADD:
                operands = getIntegerOperands(expression);
                integerResult = operands[0] + operands[1];
                System.out.println("Result of " + operands[0] + " + " + operands[1] + " is " + integerResult);
                InterpreterVisitor.setReturnSymbol(new IntSymbol(integerResult));
                break;
            case SUBTRACT:
                operands = getIntegerOperands(expression);
                integerResult = operands[0] + operands[1];
                System.out.println("Result of " + operands[0] + " - " + operands[1] + " is " + integerResult);
                InterpreterVisitor.setReturnSymbol(new IntSymbol(integerResult));
                break;
            case DIVIDE:
                operands = getIntegerOperands(expression);
                
                if(operands[1] == 0){
                    throw new ArithmeticException("Division by zero!");
                }
                integerResult = operands[0] / operands[1];
                System.out.println("Result of " + operands[0] + " / " + operands[1] + " is " + integerResult);
                InterpreterVisitor.setReturnSymbol(new IntSymbol(integerResult));
                break;
            case MULTIPLY: 
                operands = getIntegerOperands(expression);
                integerResult = operands[0] * operands[1];
                System.out.println("Result of " + operands[0] + " * " + operands[1] + " is " + integerResult);
                InterpreterVisitor.setReturnSymbol(new IntSymbol(integerResult));
                break;
            case NEGATE:
                expression.visitChild(new InterpreterDecider(), expression.getFirstOperand());
                integerResult = -((IntSymbol)InterpreterVisitor.getReturnSymbol()).getValue();
                System.out.println("Result of -" + -integerResult + " is " + integerResult);    //funky, but it works
                InterpreterVisitor.setReturnSymbol(new IntSymbol(integerResult));
                break;
            case LESS_THAN:
                operands = getIntegerOperands(expression);
                booleanResult = operands[0] < operands[1];
                System.out.println("Result of " + operands[0] + " < " + operands[1] + " is " + booleanResult);
                InterpreterVisitor.setReturnSymbol(new BooleanSymbol(booleanResult));
                break;
            case LESS_OR_EQUALS:
                operands = getIntegerOperands(expression);
                booleanResult = operands[0] <= operands[1];
                System.out.println("Result of " + operands[0] + " <= " + operands[1] + " is " + booleanResult);
                InterpreterVisitor.setReturnSymbol(new BooleanSymbol(booleanResult));
                break;
            case GREATER_THAN:
                operands = getIntegerOperands(expression);
                booleanResult = operands[0] >= operands[1];
                System.out.println("Result of " + operands[0] + " > " + operands[1] + " is " + booleanResult);
                InterpreterVisitor.setReturnSymbol(new BooleanSymbol(booleanResult));
                break;
            case GREATER_OR_EQUALS: 
                operands = getIntegerOperands(expression);
                booleanResult = operands[0] >= operands[1];
                System.out.println("Result of " + operands[0] + " >= " + operands[1] + " is " + booleanResult);
                InterpreterVisitor.setReturnSymbol(new BooleanSymbol(booleanResult));
                break;
            case EQUALS:
                expression.visitChild(new InterpreterDecider(), expression.getFirstOperand());
                
                PrimitiveSymbol firstEq = ((PrimitiveSymbol)InterpreterVisitor.getReturnSymbol());

                expression.visitChild(new InterpreterDecider(), expression.getSecondOperand());

                PrimitiveSymbol secondEq = ((PrimitiveSymbol)InterpreterVisitor.getReturnSymbol());

                booleanResult = secondEq.getValue().equals(firstEq.getValue());
                
                InterpreterVisitor.setReturnSymbol(new BooleanSymbol(booleanResult));
                System.out.println("Result of " + firstEq.getValue() + " EQUALS " + secondEq.getValue() + " is " + booleanResult + ".");
                break;
            case NOT_EQUALS:
            expression.visitChild(new InterpreterDecider(), expression.getFirstOperand());
                
                PrimitiveSymbol firstNeq = ((PrimitiveSymbol)InterpreterVisitor.getReturnSymbol());

                expression.visitChild(new InterpreterDecider(), expression.getSecondOperand());

                PrimitiveSymbol secondNeq = ((PrimitiveSymbol)InterpreterVisitor.getReturnSymbol());

                booleanResult = !(secondNeq.getValue().equals(firstNeq.getValue()));
                break;
            case OR:
                expression.visitChild(new InterpreterDecider(), expression.getFirstOperand());
                Boolean orOne = ((BooleanSymbol)InterpreterVisitor.getReturnSymbol()).getValue();
                Boolean orTwo = true;
                if(!orOne){     //only evaluating the second expression if this one is true to implement short-circuiting!
                    expression.visitChild(new InterpreterDecider(), expression.getFirstOperand());
                    orTwo = ((BooleanSymbol)InterpreterVisitor.getReturnSymbol()).getValue();
                }
                InterpreterVisitor.setReturnSymbol(new BooleanSymbol(orOne || orTwo));

                System.out.println("Result of " + orOne + " OR " + orTwo + " is " + (orOne || orTwo));
                break;
            case AND:
                expression.visitChild(new InterpreterDecider(), expression.getFirstOperand());
                Boolean andOne = ((BooleanSymbol)InterpreterVisitor.getReturnSymbol()).getValue();
                Boolean andTwo  = false;;
                if(andOne){     //only evaluating the second expression if this one is true to implement short-circuiting!
                    expression.visitChild(new InterpreterDecider(), expression.getFirstOperand());
                    andTwo = ((BooleanSymbol)InterpreterVisitor.getReturnSymbol()).getValue();
                }
                InterpreterVisitor.setReturnSymbol(new BooleanSymbol(andOne && andTwo));

                System.out.println("Result of " + andOne + " AND " + andTwo + " is " + (andOne && andTwo));
                break;
            case NOT:
                expression.visitChild(new InterpreterDecider(), expression.getFirstOperand());
                System.out.println("Negating boolean.");
                InterpreterVisitor.setReturnSymbol(new BooleanSymbol(!((BooleanSymbol)InterpreterVisitor.getReturnSymbol()).getValue()));
                break;
            case CONCAT:
                expression.visitChild(new InterpreterDecider(), expression.getFirstOperand());
                String stringOne = ((StringSymbol)InterpreterVisitor.getReturnSymbol()).getValue();
                expression.visitChild(new InterpreterDecider(), expression.getSecondOperand());
                String stringTwo = ((StringSymbol)InterpreterVisitor.getReturnSymbol()).getValue();
                System.out.println("Concatenating \"" + stringOne + "\" with \"" + stringOne + "\".");
                InterpreterVisitor.setReturnSymbol(new StringSymbol(stringOne + stringTwo));
                break;
            case VARIABLE:
                //TODO: WARNING:THIS OPERATOR IS UNSUPPORTED
                //TODO: figure out how to handle this in the interpreter!
                //at the time of writing, there is no syntactic support for this operator
                System.out.println("Converting variable to string.");
                break;
            case CONSTANT:
                //this operator always returns a primitive!
                System.out.println("Fetching constant.");
                InterpreterVisitor.setReturnSymbol(expression.getConstant());
                break;
            case ACCESS:
                expression.visitChild(new InterpreterDecider(), expression.getFirstOperand());
                TemplateSymbol template = (TemplateSymbol)InterpreterVisitor.getReturnSymbol();

                //remember the field to find in the template
                String fieldName = ((TField) expression.getSecondOperand()).getFieldName();

                ArrayList<String> map = Scope.getTemplateMapTable().get(template.getType());         //get the arraylist with the chosen template's fields

                SymbolTableEntry fieldContent = template.getContent().get(map.indexOf(fieldName));   //find the field we need with the map and get the content of the field
                System.out.println("Accessing template with \"" + fieldName + "\".");
                InterpreterVisitor.setReturnSymbol(fieldContent);

                break;
            case INDEX:
                expression.visitChild(new InterpreterDecider(), expression.getFirstOperand());
                ArrayList<SymbolTableEntry> content = ((ArraySymbol)InterpreterVisitor.getReturnSymbol()).getContent();
                expression.visitChild(new InterpreterDecider(), expression.getSecondOperand());
                Integer index = ((IntSymbol)InterpreterVisitor.getReturnSymbol()).getValue();
                InterpreterVisitor.setReturnSymbol(content.get(index));
                System.out.println("Indexing array with \"" + index + "\".");
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private Integer[] getIntegerOperands(Expression node){
        Integer[] operands = new Integer[2];
        node.visitChild(new InterpreterDecider(), node.getFirstOperand());
        operands[0] = ((PrimitiveSymbol<Integer>)InterpreterVisitor.getReturnSymbol()).getValue();
        node.visitChild(new InterpreterDecider(), node.getSecondOperand());
        operands[1] = ((PrimitiveSymbol<Integer>)InterpreterVisitor.getReturnSymbol()).getValue();
        return operands;
    }

    @SuppressWarnings("unchecked")
    private Boolean[] getBooleanOperands(Expression node){
        Boolean[] operands = new Boolean[2];
        node.visitChild(new InterpreterDecider(), node.getFirstOperand());
        operands[0] = ((PrimitiveSymbol<Boolean>)InterpreterVisitor.getReturnSymbol()).getValue();
        node.visitChild(new InterpreterDecider(), node.getSecondOperand());
        operands[1] = ((PrimitiveSymbol<Boolean>)InterpreterVisitor.getReturnSymbol()).getValue();
        return operands;
    }

} 
