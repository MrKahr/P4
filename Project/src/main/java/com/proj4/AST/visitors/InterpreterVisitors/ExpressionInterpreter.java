package com.proj4.AST.visitors.InterpreterVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Expression;
import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.symbolTable.symbols.*;

public class ExpressionInterpreter extends InterpreterVisitor {

    @SuppressWarnings("unchecked")  //typecasting without respecting generics is ok here, because we've already typechecked the program at this point
    public void visit(AST node) {
        // Expression: primitive symbol + expression operator left operand, right operand. 
        Expression expression = (Expression) node;
        
        // Note: These operators always return primitive types
        // Note: Fallthrough for each operator type
        switch (expression.getOperator()) {
            case ADD:
                expression.visitChild(new InterpreterDecider(), expression.getFirstOperand());
                Integer operandOne = ((PrimitiveSymbol<Integer>)InterpreterVisitor.getReturnSymbol()).getValue();
                expression.visitChild(new InterpreterDecider(), expression.getSecondOperand());
                Integer operandTwo = ((PrimitiveSymbol<Integer>)InterpreterVisitor.getReturnSymbol()).getValue();
                Integer sum = operandOne + operandTwo;
                System.out.println("Result of " + operandOne + " + " + operandTwo + " is " + sum);
                InterpreterVisitor.setReturnSymbol(new IntSymbol(sum));
                System.out.println("Calculated sum of " + operandOne + " and " + operandTwo + ": It's " + sum + ".");
                break;
            case SUBTRACT:

                break;
            case DIVIDE:

                break;
            case MULTIPLY: 
            
                break;
            case NEGATE:
                
                break;
            case LESS_THAN:

                break;
            case LESS_OR_EQUALS:
                
                break;
            case GREATER_THAN:
            
                break;
            case GREATER_OR_EQUALS: 
            
                break;
            case EQUALS:
            
                break;
            case NOT_EQUALS:
             
                break;
            case OR:
                
                break;
            case AND:
            
                break;
            case NOT:
            
                break;
            case CONCAT:
            
                break;
            case VARIABLE:
                
                break;
            case CONSTANT:
                //this operator always returns a primitive!
                InterpreterVisitor.setReturnSymbol(expression.getConstant());
                break;
            case ACCESS:

                break;
            case INDEX:
            
                break;
            default:
                break;
        }
    }
} 
