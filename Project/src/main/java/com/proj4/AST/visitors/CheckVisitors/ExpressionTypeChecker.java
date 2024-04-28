package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Expression;
import com.proj4.AST.nodes.Variable;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.AST.visitors.VisitorDecider;
import com.proj4.exceptions.MismatchedTypeException;
import com.proj4.exceptions.UndefinedArrayExpection;
import com.proj4.symbolTable.Scope;

public class ExpressionTypeChecker extends TypeCheckVisitor {

    public void visit(AST node) {
        Expression expression = (Expression) node;
        expression.inheritScope();
        // Note: These operators always return primitive types
        // Note: Fallthrough for each operator type
        switch (expression.getOperator()) {
            case ADD:
                // falltrough
            case SUBTRACT:
                // falltrough
            case DIVIDE:
                // falltrough
            case MULTIPLY: // all above are binary and return and consume integers
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
                // falltrough
            case LESS_OR_EQUALS:
                // falltrough
            case GREATER_THAN:
                // falltrough
            case GREATER_OR_EQUALS: // all above are binary and return booleans and consume integers
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
                // fallthrough
            case NOT_EQUALS: // all above consume two identical types and return booleans
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());
                String firstType = TypeCheckVisitor.getFoundType();
                expression.visitChild(new CheckDecider(), expression.getSecondOperand());
                if (!firstType.equals(TypeCheckVisitor.getFoundType())) {
                    throw new MismatchedTypeException();
                }
                TypeCheckVisitor.setFoundType("Boolean", "Primitive");
                break;
            case OR:
                // falltrough
            case AND: // all above are binary and consume and return booleans
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
            case INDEX:
                // Ensure that we can lookup arrays in the current scope if we have an array
                // operand
                expression.inheritScope();
                String arrayIdentifier = "";

                // Case 1: Check whether first operand is an array that is declared in scope
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());
                if (expression.getFirstOperand() instanceof Variable) {
                    // Check whether operand is defined in Vtable
                    try {
                        arrayIdentifier = ((Variable) expression.getFirstOperand()).getIdentifier();
                        expression.getScope().getVTable().get(arrayIdentifier);
                    } catch (Exception e) {
                        throw new UndefinedArrayExpection(
                                "Array\"" + arrayIdentifier + "\"in not defined in current scope");
                    }

                    // Check whether operand is an array 
                    if (!TypeCheckVisitor.getFoundComplexType().equals("Array")) {
                        throw new MismatchedTypeException(
                                "Error on indexing: Cannot index element that is not an array!");
                    }
                }

                // Case 2: Check whether second operand is an array defined in scope that can evaluate to an integer. 
                expression.visitChild(new CheckDecider(), expression.getSecondOperand());
                if (expression.getSecondOperand() instanceof Variable) {
                    // Check whether operand is defined in Vtable
                    try {
                        arrayIdentifier = ((Variable) expression.getSecondOperand()).getIdentifier();
                        expression.getScope().getVTable().get(arrayIdentifier);
                    } catch (Exception e) {
                        throw new UndefinedArrayExpection(
                                "Array\"" + arrayIdentifier + "\"in not defined in current scope");
                    }

                    // Check whether array can be evaluated to an integer.
                    if (!(TypeCheckVisitor.getFoundComplexType().equals("Array") && TypeCheckVisitor.getFoundType().equals("Integer"))) {
                        throw new MismatchedTypeException(
                                "Error on indexing: Cannot index element that is not an array!");
                    }
                }

                // Case 3: Check that index i.e. second operand is an integer
                if (!TypeCheckVisitor.getFoundType().equals("Integer")) {
                    throw new MismatchedTypeException("Index for array (or template) is not integer!");
                }
                break;
            default:
                break;

        }
    }
}