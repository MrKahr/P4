package com.proj4.AST.visitors.InterpreterVisitors;

import java.util.ArrayList;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Expression;
import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.AST.visitors.InterpreterVisitor;
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
                if(InterpreterVisitor.getReturnSymbol().getType().equals("Integer")){   //if it's not an integer, it must be a boolean
                    Integer equalsIntegerOne = ((IntSymbol)InterpreterVisitor.getReturnSymbol()).getValue();
                    expression.visitChild(new InterpreterDecider(), expression.getSecondOperand());
                    Integer equalsIntegerTwo = ((IntSymbol)InterpreterVisitor.getReturnSymbol()).getValue();
                    
                } else {
                    Boolean equalsIntegerOne = ((BooleanSymbol)InterpreterVisitor.getReturnSymbol()).getValue();
                    expression.visitChild(new InterpreterDecider(), expression.getSecondOperand());
                    Boolean equalsIntegerTwo = ((BooleanSymbol)InterpreterVisitor.getReturnSymbol()).getValue();
                    
                }
                break;
            case NOT_EQUALS:
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
                InterpreterVisitor.setReturnSymbol(new BooleanSymbol(!((BooleanSymbol)InterpreterVisitor.getReturnSymbol()).getValue()));
                break;
            case CONCAT:
                expression.visitChild(new InterpreterDecider(), expression.getFirstOperand());
                String stringOne = ((StringSymbol)InterpreterVisitor.getReturnSymbol()).getValue();
                expression.visitChild(new InterpreterDecider(), expression.getSecondOperand());
                String stringTwo = ((StringSymbol)InterpreterVisitor.getReturnSymbol()).getValue();
                InterpreterVisitor.setReturnSymbol(new StringSymbol(stringOne + stringTwo));
                break;
            case VARIABLE:
                //TODO: figure out how to handle this in the interpreter and typechecker!!!
                break;
            case CONSTANT:
                //this operator always returns a primitive!
                InterpreterVisitor.setReturnSymbol(expression.getConstant());
                break;
            case ACCESS:
                expression.visitChild(new InterpreterDecider(), expression.getFirstOperand());
                TemplateSymbol template = (TemplateSymbol)InterpreterVisitor.getReturnSymbol();

                expression.visitChild(new InterpreterDecider(), expression.getSecondOperand());      //TODO: find out how exactly to handle the second operand
                //remember the field to find in the template
                String fieldName = ((StringSymbol)InterpreterVisitor.getReturnSymbol()).getValue();  //TODO: USING THIS AS A PLACEHOLDER UNTIL THE SECOND OPERAND GETS IMPLEMENTED PROPERLY!

                ArrayList<String> map = Scope.getTemplateMapTable().get(template.getType());         //get the arraylist with the chosen template's fields

                SymbolTableEntry fieldContent = template.getContent().get(map.indexOf(fieldName));   //find the field we need with the map and get the content of the field

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
