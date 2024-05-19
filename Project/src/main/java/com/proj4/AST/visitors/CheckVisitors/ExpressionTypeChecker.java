package com.proj4.AST.visitors.CheckVisitors;

import java.util.ArrayList;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Expression;
import com.proj4.AST.nodes.ExpressionOperator;
import com.proj4.AST.nodes.StringCast;
import com.proj4.AST.nodes.TField;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.*;
import com.proj4.symbolTable.GlobalScope;
import com.proj4.symbolTable.symbols.ArraySymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;
import com.proj4.symbolTable.symbols.TemplateSymbol;

public class ExpressionTypeChecker extends TypeCheckVisitor {

    public void visit(AST node) {
        Expression expression = (Expression) node;

        // Note: These operators always return primitive types  //new StringCast(expression);
        // Note: Fallthrough for each operator type
        String firstType;
        String secondType;
        switch (expression.getOperator()) {
            case ADD:
            /*
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());
                if (!TypeCheckVisitor.getFoundType().equals("Integer") && !TypeCheckVisitor.getFoundType().equals("String")) {
                    throw new MismatchedTypeException();
                }
                //if any operand is a string, we know we'll be concatenating strings instead of adding numbers
                if (TypeCheckVisitor.getFoundType().equals("String")) {
                    TypeCheckVisitor.setFoundType("String", "Primitive", 0);
                }

                expression.visitChild(new CheckDecider(), expression.getSecondOperand());
                if (!TypeCheckVisitor.getFoundType().equals("Integer") && !TypeCheckVisitor.getFoundType().equals("String")) {
                    throw new MismatchedTypeException();
                }
                if (TypeCheckVisitor.getFoundType().equals("String")) {
                    TypeCheckVisitor.setFoundType("String", "Primitive", 0);
                } else {
                    TypeCheckVisitor.setFoundType("Integer", "Primitive", 0);
                }
            */
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());
                firstType = TypeCheckVisitor.getFoundType();
                expression.visitChild(new CheckDecider(), expression.getSecondOperand());
                secondType = TypeCheckVisitor.getFoundType();

                if(firstType.equals("Integer") && secondType.equals("Integer")){
                    //both operands are integers. The output is an integer
                    TypeCheckVisitor.setFoundType("Integer", "Primitive", 0);

                } else if (firstType.equals("String") && secondType.equals("Integer")) {
                    //first operand is a string. Cast second operand to string and set operator to CONCAT. Output is a string
                    new StringCast(expression.getSecondOperand());
                    TypeCheckVisitor.setFoundType("String", "Primitive", 0);
                    expression.setOperator(ExpressionOperator.CONCAT);

                } else if (firstType.equals("Integer") && secondType.equals("String")){
                    //second operand is a string. Cast first operand to string. Output is a string
                    new StringCast(expression.getFirstOperand());
                    TypeCheckVisitor.setFoundType("String", "Primitive", 0);
                    expression.setOperator(ExpressionOperator.CONCAT);

                } else if (firstType.equals("String") && secondType.equals("String")){
                    //both operands are strings. Output is a string
                    TypeCheckVisitor.setFoundType("String", "Primitive", 0);
                    expression.setOperator(ExpressionOperator.CONCAT);
                } else {
                    //Something that's not a string or integer has been found. Throw
                    throw new MismatchedTypeException();
                }
                    
                break;
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
                TypeCheckVisitor.setFoundType("Integer", "Primitive", 0);
                break;
            case NEGATE:
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());
                if (!TypeCheckVisitor.getFoundType().equals("Integer")) {
                    throw new MismatchedTypeException();
                }
                TypeCheckVisitor.setFoundType("Integer", "Primitive", 0);
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
                TypeCheckVisitor.setFoundType("Boolean", "Primitive", 0);
                break;
            case EQUALS:
                // fallthrough
            case NOT_EQUALS: // all above consume two identical types and return booleans
                //TODO: make sure this uses complex types and nesting level!!!
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());
                firstType = TypeCheckVisitor.getFoundType();
                expression.visitChild(new CheckDecider(), expression.getSecondOperand());
                if (!firstType.equals(TypeCheckVisitor.getFoundType())) {
                    throw new MismatchedTypeException();
                }
                TypeCheckVisitor.setFoundType("Boolean", "Primitive", 0);
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
                TypeCheckVisitor.setFoundType("Boolean", "Primitive", 0);
                break;
            case NOT:
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());
                if (!TypeCheckVisitor.getFoundType().equals("Boolean")) {
                    throw new MismatchedTypeException();
                }
                TypeCheckVisitor.setFoundType("Boolean", "Primitive", 0);
                break;
            case VARIABLE:
                //TODO: this case is unused, as its functionality is partially implemented by cCONCAT
                TypeCheckVisitor.setFoundType("String", "Primitive", 0);
                break;
            case CONSTANT:
                TypeCheckVisitor.setFoundType(expression.getConstant().getType(), "Primitive", 0);
                break;
            case ACCESS:
                //make sure the first operand is actually a template
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());    
                if (!TypeCheckVisitor.getFoundComplexType().equals("Template")) {
                    throw new MismatchedTypeException();
                }
                //remember the type of the template we found
                String templateType = TypeCheckVisitor.getFoundType();

                //make sure the second operand specifies a field to index
                TField templateField = null;
                try {
                    templateField  = (TField) expression.getSecondOperand();
                } catch (ClassCastException cce) {
                    throw new MalformedAstException("Invalid template field provided.");
                }

                //remember the field to find in the template
                String fieldName = templateField.getFieldName();

                ArrayList<String> map = GlobalScope.getInstance().getTemplateMapTable().get(templateType);    //get the arraylist with the chosen template's fields
                TemplateSymbol blueprint = GlobalScope.getInstance().getBlueprintTable().get(templateType); //get the blueprint for the chosen template

                SymbolTableEntry fieldContent = blueprint.getContent().get(map.indexOf(fieldName));  //find the field we need with the map and get the content of the field

                if (fieldContent.getComplexType().equals("Array")) {
                    ArraySymbol fieldArray = (ArraySymbol) fieldContent;
                    TypeCheckVisitor.setFoundType(fieldContent.getType(), "Array", fieldArray.getNestingLevel());
                } else {
                    TypeCheckVisitor.setFoundType(fieldContent.getType(), fieldContent.getComplexType(), 0);
                }
                
                //TODO: When we finally get to interpreting the program, the process of getting the field will be quite similar, 
                //TODO: the difference being that we use the TemplateSymbol returned by the first operand instead of using the blueprint for the template type
            case INDEX:
                // Case 1: Check whether first operand is an array that is declared in scope
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());
                // Check whether operand is an array 
                if (!TypeCheckVisitor.getFoundComplexType().equals("Array")) {
                    throw new MismatchedTypeException(
                        "Error on indexing: Cannot index element that is not an array!");
                    }
                //remember array's type and nestinglevel
                String arrayType = TypeCheckVisitor.getFoundType();
                int nestingLevel = TypeCheckVisitor.getNestingLevel();

                // Case 2: Check that index i.e. second operand is an integer
                if (!(TypeCheckVisitor.getFoundType().equals("Integer") && TypeCheckVisitor.getFoundComplexType().equals("Primitive"))) {
                    throw new MismatchedTypeException("Index for array (or template) is not integer!");
                }

                if (TypeCheckVisitor.getNestingLevel() > 0) {   //if nestingLevel is 0, indexing will give us something that is not an array
                    TypeCheckVisitor.setFoundType(arrayType, "Array", nestingLevel - 1);
                } else {
                    switch (arrayType) {
                        case "Integer":
                        case "Boolean":
                        case "String":
                            TypeCheckVisitor.setFoundType(arrayType, "Primitive", 0);
                        break;
                        default:
                            TypeCheckVisitor.setFoundType(arrayType, "Template", 0);
                            break;
                    }
                }
                break;
            default:
                break;
        }
    }
}