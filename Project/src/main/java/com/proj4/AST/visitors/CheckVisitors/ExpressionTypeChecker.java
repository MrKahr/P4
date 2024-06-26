package com.proj4.AST.visitors.CheckVisitors;

import java.util.ArrayList;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Expression;
import com.proj4.AST.nodes.ExpressionOperator;
import com.proj4.AST.nodes.StringCast;
import com.proj4.AST.nodes.TField;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.*;
import com.proj4.symbolTable.GlobalScope;
import com.proj4.symbolTable.symbols.ArraySymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;
import com.proj4.symbolTable.symbols.TemplateSymbol;

public class ExpressionTypeChecker implements NodeVisitor {

    public void visit(AST node) {
        Expression expression = (Expression) node;

        // Note: These operators always return primitive types  //new StringCast(expression);
        // Note: Fallthrough for each operator type
        String firstType;
        String secondType;
        switch (expression.getOperator()) {
            case ADD:
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());
                firstType = TypeCheckVisitor.getInstance().getFoundType();
                String firstComplexType = TypeCheckVisitor.getInstance().getFoundComplexType();
                expression.visitChild(new CheckDecider(), expression.getSecondOperand());
                secondType = TypeCheckVisitor.getInstance().getFoundType();
                String secondComplexType = TypeCheckVisitor.getInstance().getFoundComplexType();

                if(firstType.equals("Integer") && secondType.equals("Integer")){
                    //both operands are integers. The output is an integer
                    TypeCheckVisitor.getInstance().setFoundType("Integer", "Primitive", -1);

                } else if (firstType.equals("String") && secondType.equals("Integer")) {
                    //first operand is a string. Cast second operand to string and set operator to CONCAT. Output is a string
                    new StringCast(expression.getSecondOperand());
                    TypeCheckVisitor.getInstance().setFoundType("String", "Primitive", -1);
                    expression.setOperator(ExpressionOperator.CONCAT);

                } else if (firstType.equals("Integer") && secondType.equals("String")){
                    //second operand is a string. Cast first operand to string. Output is a string
                    new StringCast(expression.getFirstOperand());
                    TypeCheckVisitor.getInstance().setFoundType("String", "Primitive", -1);
                    expression.setOperator(ExpressionOperator.CONCAT);

                } else if (firstType.equals("String") && secondType.equals("String")){
                    //both operands are strings. Output is a string
                    TypeCheckVisitor.getInstance().setFoundType("String", "Primitive", -1);

                    expression.setOperator(ExpressionOperator.CONCAT);
                } else {
                    //Something that's not a string or integer has been found. Throw
                    throw new MismatchedTypeException("Both operands are not a String or an Integer! First operand has type \"" + firstType + "\", complex type \"" + firstComplexType + "\""
                                                      + " second operand has type \"" + secondType + "\", complex type " + secondComplexType + "\"");
                }

                break;
            case SUBTRACT:
                // falltrough
            case DIVIDE:
                // falltrough
            case MULTIPLY: // all above are binary and return and consume integers
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());
                if (!TypeCheckVisitor.getInstance().getFoundType().equals("Integer")) {
                    throw new MismatchedTypeException("Invalid type! Cannot perform arithmetic on operands."
                                                      + " Operand 1 has type \"" + TypeCheckVisitor.getInstance().getFoundType()
                                                      + "\", complex type \"" + TypeCheckVisitor.getInstance().getFoundComplexType()
                                                      + "\"");
                }
                expression.visitChild(new CheckDecider(), expression.getSecondOperand());
                if (!TypeCheckVisitor.getInstance().getFoundType().equals("Integer")) {
                    throw new MismatchedTypeException("Invalid type! Cannot perform arithmetic on operands."
                    + " Operand 2 has type \"" + TypeCheckVisitor.getInstance().getFoundType()
                    + "\", complex type \"" + TypeCheckVisitor.getInstance().getFoundComplexType()
                    + "\"");
                }
                TypeCheckVisitor.getInstance().setFoundType("Integer", "Primitive", -1);
                break;
            case NEGATE:
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());
                if (!TypeCheckVisitor.getInstance().getFoundType().equals("Integer")) {
                    throw new MismatchedTypeException("Invalid type! Cannot negate operand."
                                                      + " Operand 1 has type \"" + TypeCheckVisitor.getInstance().getFoundType()
                                                      + "\", complex type \"" + TypeCheckVisitor.getInstance().getFoundComplexType()
                                                      + "\"");
                }
                TypeCheckVisitor.getInstance().setFoundType("Integer", "Primitive", -1);
                break;
            case LESS_THAN:
                // falltrough
            case LESS_OR_EQUALS:
                // falltrough
            case GREATER_THAN:
                // falltrough
            case GREATER_OR_EQUALS: // all above are binary and return booleans and consume integers
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());
                if (!TypeCheckVisitor.getInstance().getFoundType().equals("Integer")) {
                    throw new MismatchedTypeException("First operand is not an integer! Type \"" + TypeCheckVisitor.getInstance().getFoundType()
                    + "\", complex type \"" + TypeCheckVisitor.getInstance().getFoundComplexType()
                    + "\", nesting level " + TypeCheckVisitor.getInstance().getNestingLevel());
                }
                expression.visitChild(new CheckDecider(), expression.getSecondOperand());
                if (!TypeCheckVisitor.getInstance().getFoundType().equals("Integer")) {
                    throw new MismatchedTypeException("Second operand is not an integer! Type \"" + TypeCheckVisitor.getInstance().getFoundType()
                    + "\", complex type \"" + TypeCheckVisitor.getInstance().getFoundComplexType()
                    + "\", nesting level " + TypeCheckVisitor.getInstance().getNestingLevel());
                }
                TypeCheckVisitor.getInstance().setFoundType("Boolean", "Primitive", -1);
                break;
            case EQUALS:
                // fallthrough
            case NOT_EQUALS: // all above consume two identical types and return booleans
                //TODO: make sure this uses complex types and nesting level!!!
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());
                firstType = TypeCheckVisitor.getInstance().getFoundType();
                expression.visitChild(new CheckDecider(), expression.getSecondOperand());
                if (!firstType.equals(TypeCheckVisitor.getInstance().getFoundType())) {
                    throw new MismatchedTypeException("First operand has type \"" + firstType + "\", second operand has type \"" + TypeCheckVisitor.getInstance().getFoundType() + "\"");
                }
                TypeCheckVisitor.getInstance().setFoundType("Boolean", "Primitive", -1);
                break;
            case OR:
                // falltrough
            case AND: // all above are binary and consume and return booleans
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());
                if (!TypeCheckVisitor.getInstance().getFoundType().equals("Boolean")) {
                    throw new MismatchedTypeException();
                }
                expression.visitChild(new CheckDecider(), expression.getSecondOperand());
                if (!TypeCheckVisitor.getInstance().getFoundType().equals("Boolean")) {
                    throw new MismatchedTypeException();
                }
                TypeCheckVisitor.getInstance().setFoundType("Boolean", "Primitive", -1);
                break;
            case NOT:
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());
                if (!TypeCheckVisitor.getInstance().getFoundType().equals("Boolean")) {
                    throw new MismatchedTypeException();
                }
                TypeCheckVisitor.getInstance().setFoundType("Boolean", "Primitive", -1);
                break;
            case VARIABLE:
                //TODO: this case is unused, as its functionality is partially implemented by CONCAT
                TypeCheckVisitor.getInstance().setFoundType("String", "Primitive", -1);
                break;
            case CONSTANT:
                TypeCheckVisitor.getInstance().setFoundType(expression.getConstant().getType(), "Primitive", -1);
                break;
            case ACCESS:
                //make sure the first operand is actually a template
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());
                String foundComplexType = TypeCheckVisitor.getInstance().getFoundComplexType();
                Integer foundNestingLevel = TypeCheckVisitor.getInstance().getNestingLevel();
                // We check nesting level to allow templates to be accessed in arrays, e.g. "deck[0].cost"
                if (!(foundComplexType.equals("Template") || foundComplexType.equals("Array") && foundNestingLevel == -1)) {
                    throw new MismatchedTypeException("First operand has complex type \"" + foundComplexType + "\" with nesting level " + foundNestingLevel + " which is not a Template");
                }
                //remember the type of the template we found
                String templateType = TypeCheckVisitor.getInstance().getFoundType();

                //make sure the second operand specifies a field to index
                TField templateField = null;
                try {
                    templateField = (TField) expression.getSecondOperand();
                } catch (ClassCastException cce) {
                    throw new MalformedAstException("Invalid template field provided.");
                }

                //remember the field to find in the template
                String fieldName = templateField.getFieldName();

                ArrayList<String> map = GlobalScope.getInstance().getTemplateMapTable().get(templateType);  //get the arraylist with the chosen template's fields
                TemplateSymbol blueprint = GlobalScope.getInstance().getBlueprintTable().get(templateType); //get the blueprint for the chosen template

                SymbolTableEntry fieldContent;
                try {
                    fieldContent = blueprint.getContent().get(map.indexOf(fieldName));  //find the field we need with the map and get the content of the field
                } catch (IndexOutOfBoundsException iofe) {
                    throw new UndefinedVariableException("Field name \"" + fieldName + "\" does not exist in template \"" + templateType + "\"");
                }

                if (fieldContent.getComplexType().equals("Array")) {
                    ArraySymbol fieldArray = (ArraySymbol) fieldContent;
                    TypeCheckVisitor.getInstance().setFoundType(fieldContent.getType(), "Array", fieldArray.getNestingLevel());
                } else {
                    TypeCheckVisitor.getInstance().setFoundType(fieldContent.getType(), fieldContent.getComplexType(), -1);
                }

                //TODO: When we finally get to interpreting the program, the process of getting the field will be quite similar,
                //TODO: the difference being that we use the TemplateSymbol returned by the first operand instead of using the blueprint for the template type
                break;
            case INDEX:
                // Case 1: Check whether first operand is an array that is declared in scope
                expression.visitChild(new CheckDecider(), expression.getFirstOperand());
                // Check whether operand is an array
                String foundComplexType_INDEX = TypeCheckVisitor.getInstance().getFoundComplexType();

                if (!(foundComplexType_INDEX.equals("Array"))) {
                    throw new MismatchedTypeException(
                        "Error on indexing: Cannot index element \"" + foundComplexType_INDEX + "\": it is not an array!");
                    }
                //remember array's type and nestinglevel
                String arrayType = TypeCheckVisitor.getInstance().getFoundType();
                int nestingLevel = TypeCheckVisitor.getInstance().getNestingLevel();

                // Case 2: Check that index i.e. second operand is an integer
                expression.visitChild(new CheckDecider(), expression.getSecondOperand());
                if (!(TypeCheckVisitor.getInstance().getFoundType().equals("Integer") && TypeCheckVisitor.getInstance().getFoundComplexType().equals("Primitive"))) {
                    throw new MismatchedTypeException("Index for array (or template) is not integer! Received type: '" + TypeCheckVisitor.getInstance().getFoundType() + "'' with complex type: '" + TypeCheckVisitor.getInstance().getFoundComplexType() + "'");
                }

                if (nestingLevel > 0) {   //if nestingLevel is -1, indexing will give us something that is not an array
                    TypeCheckVisitor.getInstance().setFoundType(arrayType, "Array", nestingLevel - 1);
                } else {
                    switch (arrayType) {
                        case "Integer":
                        case "Boolean":
                        case "String":
                            TypeCheckVisitor.getInstance().setFoundType(arrayType, "Primitive", -1);
                        break;
                        default:
                            TypeCheckVisitor.getInstance().setFoundType(arrayType, "Template", -1);
                            break;
                    }
                }
                break;
            default:
                break;
        }
    }
}