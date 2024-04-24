package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Expression;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.MismatchedTypeException;

public class ExpressionTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        Expression expression = (Expression) node;
        expression.inheritScope();
        //Note: These operators always return primitive types
        //Note: Fallthrough for each operator type 
        switch (expression.getOperator()) {
            case ADD:
            //falltrough
            case SUBTRACT:
            //falltrough
            case DIVIDE:
            //falltrough
            case MULTIPLY:   //all above are binary and return and consume integers
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());    
                if (!TypeCheckVisitor.getFoundType().equals("Integer")) {
                    throw new MismatchedTypeException();
                }
                expression.visitChild(new CheckDecider(), expression.getSecondOperand());
                if (!TypeCheckVisitor.getFoundType().equals("Integer")) {
                    throw new MismatchedTypeException();
                }
                TypeCheckVisitor.setFoundType("Integer", "Primitive");
                break;
            case NEGATE:
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());    
                if (!TypeCheckVisitor.getFoundType().equals("Integer")) {
                    throw new MismatchedTypeException();
                }
                TypeCheckVisitor.setFoundType("Integer", "Primitive");
                break;            
            case LESS_THAN:
            //falltrough   
            case LESS_OR_EQUALS:
            //falltrough   
            case GREATER_THAN:
            //falltrough
            case GREATER_OR_EQUALS:  //all above are binary and return booleans and consume integers
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());    
                if (!TypeCheckVisitor.getFoundType().equals("Integer")) {
                    throw new MismatchedTypeException();
                }
                expression.visitChild(new CheckDecider(), expression.getSecondOperand());
                if (!TypeCheckVisitor.getFoundType().equals("Integer")) {
                    throw new MismatchedTypeException();
                }
                TypeCheckVisitor.setFoundType("Boolean", "Primitive");
                break;
            case EQUALS:
            //fallthrough
            case NOT_EQUALS: //all above consume two identical types and return booleans
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());
                String firstType = TypeCheckVisitor.getFoundType();
                expression.visitChild(new CheckDecider(), expression.getSecondOperand());
                if (!firstType.equals(TypeCheckVisitor.getFoundType())) {
                    throw new MismatchedTypeException();
                }
                TypeCheckVisitor.setFoundType("Boolean", "Primitive");
                break;
            case OR:
            //falltrough
            case AND:    //all above are binary and consume and return booleans
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());    
                if (!TypeCheckVisitor.getFoundType().equals("Boolean")) {
                    throw new MismatchedTypeException();
                }
                expression.visitChild(new CheckDecider(), expression.getSecondOperand());
                if (!TypeCheckVisitor.getFoundType().equals("Boolean")) {
                    throw new MismatchedTypeException();
                }
                TypeCheckVisitor.setFoundType("Boolean", "Primitive");
                break;
            case NOT:
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());    
                if (!TypeCheckVisitor.getFoundType().equals("Boolean")) {
                    throw new MismatchedTypeException();
                }
                TypeCheckVisitor.setFoundType("Boolean", "Primitive");
                break;
            case CONCAT:
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());    
                if (!TypeCheckVisitor.getFoundType().equals("String")) {
                    throw new MismatchedTypeException();
                }
                expression.visitChild(new CheckDecider(), expression.getSecondOperand());
                if (!TypeCheckVisitor.getFoundType().equals("String")) {
                    throw new MismatchedTypeException();
                }
                TypeCheckVisitor.setFoundType("String", "Primitive");
                break;
            case VARIABLE:
                TypeCheckVisitor.setFoundType("String", "Primitive");
                break;
            case CONSTANT:
                TypeCheckVisitor.setFoundType(expression.getConstant().getType(), "Primitive");
                break;
            default:
                break;

        }
    }
}