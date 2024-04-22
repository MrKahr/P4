package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Expression;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.MismatchedTypeException;
import com.proj4.AST.nodes.ExpressionOperator;

public class ExpressionTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        Expression expression = (Expression) node;
        expression.inheritScope();

        //Note: Fallthrough for each operator type 
        switch (expression.getOperator()) {
            case ExpressionOperator.ADD:
            //falltrough
            case ExpressionOperator.SUBTRACT:
            //falltrough
            case ExpressionOperator.DIVIDE:
            //falltrough
            case ExpressionOperator.MULTIPLY:   //all above are binary and return and consume integers
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());    
                if (!TypeCheckVisitor.getFoundType().equals("Integer")) {
                    throw new MismatchedTypeException();
                }
                expression.visitChild(new CheckDecider(), expression.getSecondOperand());
                if (!TypeCheckVisitor.getFoundType().equals("Integer")) {
                    throw new MismatchedTypeException();
                }
                TypeCheckVisitor.setFoundType("Integer");
                break;
            case ExpressionOperator.NEGATE:
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());    
                if (!TypeCheckVisitor.getFoundType().equals("Integer")) {
                    throw new MismatchedTypeException();
                }
                TypeCheckVisitor.setFoundType("Integer");
                break;            
            case ExpressionOperator.LESS_THAN:
            //falltrough   
            case ExpressionOperator.LESS_OR_EQUALS:
            //falltrough   
            case ExpressionOperator.GREATER_THAN:
            //falltrough
            case ExpressionOperator.GREATER_OR_EQUALS:  //all above are binary and return booleans and consume integers
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());    
                if (!TypeCheckVisitor.getFoundType().equals("Integer")) {
                    throw new MismatchedTypeException();
                }
                expression.visitChild(new CheckDecider(), expression.getSecondOperand());
                if (!TypeCheckVisitor.getFoundType().equals("Integer")) {
                    throw new MismatchedTypeException();
                }
                TypeCheckVisitor.setFoundType("Boolean");
                break;
            case ExpressionOperator.EQUALS:
            //fallthrough
            case ExpressionOperator.NOT_EQUALS: //all above consume two identical types and return booleans
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());
                String firstType = TypeCheckVisitor.getFoundType();
                expression.visitChild(new CheckDecider(), expression.getSecondOperand());
                if (!firstType.equals(TypeCheckVisitor.getFoundType())) {
                    throw new MismatchedTypeException();
                }
                TypeCheckVisitor.setFoundType("Boolean");
                break;
            case ExpressionOperator.OR:
            //falltrough
            case ExpressionOperator.AND:    //all above are binary and consume and return booleans
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());    
                if (!TypeCheckVisitor.getFoundType().equals("Boolean")) {
                    throw new MismatchedTypeException();
                }
                expression.visitChild(new CheckDecider(), expression.getSecondOperand());
                if (!TypeCheckVisitor.getFoundType().equals("Boolean")) {
                    throw new MismatchedTypeException();
                }
                TypeCheckVisitor.setFoundType("Boolean");
                break;
            case ExpressionOperator.NOT:
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());    
                if (!TypeCheckVisitor.getFoundType().equals("Boolean")) {
                    throw new MismatchedTypeException();
                }
                TypeCheckVisitor.setFoundType("Boolean");
                break;
            case ExpressionOperator.CONCAT:
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());    
                if (!TypeCheckVisitor.getFoundType().equals("String")) {
                    throw new MismatchedTypeException();
                }
                expression.visitChild(new CheckDecider(), expression.getSecondOperand());
                if (!TypeCheckVisitor.getFoundType().equals("String")) {
                    throw new MismatchedTypeException();
                }
                TypeCheckVisitor.setFoundType("String");
                break;
            case ExpressionOperator.VARIABLE:
                TypeCheckVisitor.setFoundType("String");
                break;
            case ExpressionOperator.CONSTANT:
                TypeCheckVisitor.setFoundType(expression.getConstant().getType());
                break;
            default:
                break;

        }
    }
}