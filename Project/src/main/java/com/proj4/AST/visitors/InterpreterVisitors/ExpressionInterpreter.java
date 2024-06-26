package com.proj4.AST.visitors.InterpreterVisitors;

import java.util.ArrayList;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Expression;
import com.proj4.AST.nodes.TField;
import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.exceptions.*;
import com.proj4.symbolTable.GlobalScope;
import com.proj4.symbolTable.symbols.*;

public class ExpressionInterpreter implements NodeVisitor {
    private Boolean verbose = false;

    public ExpressionInterpreter(){}
    public ExpressionInterpreter(Boolean verbose){
        this.verbose = verbose;
    }

    @SuppressWarnings({ "rawtypes" })  //typecasting without respecting generics is ok here, because we've already typechecked the program at this point
    public void visit(AST node) {
        // Expression: primitive symbol + expression operator left operand, right operand.
        Expression expression = (Expression) node;
        Integer integerResult;
        Boolean booleanResult;
        Integer operands[];
        // Note: These operators always return primitive types
        switch (expression.getOperator()) {
            case ADD:
                //this handles both concatenation and addition. We concatenate if at least one operand is a string
                expression.visitChild(new InterpreterDecider(), expression.getFirstOperand());
                SymbolTableEntry addArgOne = InterpreterVisitor.getInstance().getReturnSymbol();
                expression.visitChild(new InterpreterDecider(), expression.getSecondOperand());
                SymbolTableEntry addArgTwo = InterpreterVisitor.getInstance().getReturnSymbol();

                Integer left = ((IntegerSymbol) addArgOne).getValue();
                Integer right = ((IntegerSymbol) addArgTwo).getValue();
                integerResult = left + right;
                if(this.verbose){
                    System.out.println(this.getClass().getSimpleName() + ": Result of " + left + " + " + right + " is " + integerResult);
                }

                InterpreterVisitor.getInstance().setReturnSymbol(new IntegerSymbol(integerResult));
                break;
            case SUBTRACT:
                operands = getIntegerOperands(expression);
                integerResult = operands[0] - operands[1];

                if(this.verbose){
                    System.out.println(this.getClass().getSimpleName() + ": Result of " + operands[0] + " - " + operands[1] + " is " + integerResult);
                }
                InterpreterVisitor.getInstance().setReturnSymbol(new IntegerSymbol(integerResult));
                break;
            case DIVIDE:
                operands = getIntegerOperands(expression);

                if(operands[1] == 0){
                    throw new ArithmeticException("Division by zero!");
                }
                integerResult = operands[0] / operands[1];

                if(this.verbose){
                    System.out.println(this.getClass().getSimpleName() + ": Result of " + operands[0] + " / " + operands[1] + " is " + integerResult);
                }
                InterpreterVisitor.getInstance().setReturnSymbol(new IntegerSymbol(integerResult));
                break;
            case MULTIPLY:
                operands = getIntegerOperands(expression);
                integerResult = operands[0] * operands[1];

                if(this.verbose){
                    System.out.println(this.getClass().getSimpleName() + ": Result of " + operands[0] + " * " + operands[1] + " is " + integerResult);
                }
                InterpreterVisitor.getInstance().setReturnSymbol(new IntegerSymbol(integerResult));
                break;
            case NEGATE:
                expression.visitChild(new InterpreterDecider(), expression.getFirstOperand());
                integerResult = -((IntegerSymbol)InterpreterVisitor.getInstance().getReturnSymbol()).getValue();

                if(this.verbose){
                    System.out.println(this.getClass().getSimpleName() + ": Result of -" + -integerResult + " is " + integerResult);    //funky, but it works
                }
                InterpreterVisitor.getInstance().setReturnSymbol(new IntegerSymbol(integerResult));
                break;
            case LESS_THAN:
                operands = getIntegerOperands(expression);
                booleanResult = operands[0] < operands[1];

                if(this.verbose){
                    System.out.println(this.getClass().getSimpleName() + ": Result of " + operands[0] + " < " + operands[1] + " is " + booleanResult);
                }
                InterpreterVisitor.getInstance().setReturnSymbol(new BooleanSymbol(booleanResult));
                break;
            case LESS_OR_EQUALS:
                operands = getIntegerOperands(expression);
                booleanResult = operands[0] <= operands[1];

                if(this.verbose){
                    System.out.println(this.getClass().getSimpleName() + ": Result of " + operands[0] + " <= " + operands[1] + " is " + booleanResult);
                }
                InterpreterVisitor.getInstance().setReturnSymbol(new BooleanSymbol(booleanResult));
                break;
            case GREATER_THAN:
                operands = getIntegerOperands(expression);
                booleanResult = operands[0] > operands[1];

                if(this.verbose){
                    System.out.println(this.getClass().getSimpleName() + ": Result of " + operands[0] + " > " + operands[1] + " is " + booleanResult);
                }
                InterpreterVisitor.getInstance().setReturnSymbol(new BooleanSymbol(booleanResult));
                break;
            case GREATER_OR_EQUALS:
                operands = getIntegerOperands(expression);
                booleanResult = operands[0] >= operands[1];
                if(this.verbose){
                    System.out.println(this.getClass().getSimpleName() + ": Result of " + operands[0] + " >= " + operands[1] + " is " + booleanResult);
                }
                InterpreterVisitor.getInstance().setReturnSymbol(new BooleanSymbol(booleanResult));
                break;
            case EQUALS: // TODO: Outdated implementation. Only works with primitives
                SymbolTableEntry firstElement;
                SymbolTableEntry secondElement;

                // Visit first element
                expression.visitChild(new InterpreterDecider(), expression.getFirstOperand());

                if(InterpreterVisitor.getInstance().getReturnSymbol().getComplexType().equals("Primitive")){
                    firstElement =  ((PrimitiveSymbol)InterpreterVisitor.getInstance().getReturnSymbol());
                } else if(InterpreterVisitor.getInstance().getReturnSymbol().getComplexType().equals("Complex")) {
                    firstElement =  ((ComplexSymbol)InterpreterVisitor.getInstance().getReturnSymbol());
                } else {
                    throw new UnexpectedTypeException("Recieved unexpected type for equals operation: " + InterpreterVisitor.getInstance().getReturnSymbol().getType() +  "");
                }
                expression.visitChild(new InterpreterDecider(), expression.getSecondOperand());

                secondElement = InterpreterVisitor.getInstance().getReturnSymbol();

                booleanResult = firstElement.equals(secondElement);

                InterpreterVisitor.getInstance().setReturnSymbol(new BooleanSymbol(booleanResult));

                //System.out.println("Result of " + firstElement.getValue() + " EQUALS " + secondEq.getValue() + " is " + booleanResult + ".");
                break;
            case NOT_EQUALS: // TODO: Outdated implementation. Only works with primitives
                SymbolTableEntry NeqfirstElement;
                SymbolTableEntry NeqsecondElement;

                // Visit first element
                expression.visitChild(new InterpreterDecider(), expression.getFirstOperand());

                SymbolTableEntry returnSymbol = InterpreterVisitor.getInstance().getReturnSymbol();

                if(returnSymbol instanceof PrimitiveSymbol){
                    NeqfirstElement = ((PrimitiveSymbol) InterpreterVisitor.getInstance().getReturnSymbol());
                } else if(returnSymbol instanceof ComplexSymbol) {
                    NeqfirstElement = ((ComplexSymbol) InterpreterVisitor.getInstance().getReturnSymbol());
                } else {
                    throw new UnexpectedTypeException("Recieved unexpected type for equals operation: \"" + returnSymbol.getType() + "\", complex type \"" + returnSymbol.getComplexType() + "\"");
                }
                expression.visitChild(new InterpreterDecider(), expression.getSecondOperand());

                NeqsecondElement = InterpreterVisitor.getInstance().getReturnSymbol();

                booleanResult = !(NeqfirstElement.equals(NeqsecondElement));

                InterpreterVisitor.getInstance().setReturnSymbol(new BooleanSymbol(booleanResult));
                break;
            case OR:
                expression.visitChild(new InterpreterDecider(), expression.getFirstOperand());
                Boolean orOne = ((BooleanSymbol)InterpreterVisitor.getInstance().getReturnSymbol()).getValue();
                Boolean orTwo = true;
                if(!orOne){     //only evaluating the second expression if this one is true to implement short-circuiting!
                    expression.visitChild(new InterpreterDecider(), expression.getSecondOperand());
                    orTwo = ((BooleanSymbol)InterpreterVisitor.getInstance().getReturnSymbol()).getValue();
                }
                InterpreterVisitor.getInstance().setReturnSymbol(new BooleanSymbol(orOne || orTwo));

                if(this.verbose){
                    System.out.println(this.getClass().getSimpleName() + ": Result of " + orOne + " OR " + orTwo + " is " + (orOne || orTwo));
                }
                break;
            case AND:
                expression.visitChild(new InterpreterDecider(), expression.getFirstOperand());
                Boolean andOne = ((BooleanSymbol)InterpreterVisitor.getInstance().getReturnSymbol()).getValue();
                Boolean andTwo = false;
                if(andOne){     //only evaluating the second expression if this one is true to implement short-circuiting!
                    expression.visitChild(new InterpreterDecider(), expression.getSecondOperand());
                    andTwo = ((BooleanSymbol)InterpreterVisitor.getInstance().getReturnSymbol()).getValue();
                }
                InterpreterVisitor.getInstance().setReturnSymbol(new BooleanSymbol(andOne && andTwo));

                if(this.verbose){
                    System.out.println(this.getClass().getSimpleName() + ": Result of " + andOne + " AND " + andTwo + " is " + (andOne && andTwo));
                }
                break;
            case NOT:
                expression.visitChild(new InterpreterDecider(), expression.getFirstOperand());

                if(this.verbose){
                    System.out.println(this.getClass().getSimpleName() + ": Negating boolean.");
                }
                InterpreterVisitor.getInstance().setReturnSymbol(new BooleanSymbol(!((BooleanSymbol)InterpreterVisitor.getInstance().getReturnSymbol()).getValue()));
                break;
            case VARIABLE:
                //TODO: WARNING:THIS OPERATOR IS UNSUPPORTED
                //TODO: figure out how to handle this in the interpreter!
                //at the time of writing, there is no syntactic support for this operator
                if(this.verbose){
                    System.out.println(this.getClass().getSimpleName() + ": Unsupported Operation: Converting variable to string.");
                }
                break;
            case CONSTANT:
                //this operator always returns a primitive!
                if(this.verbose){
                    System.out.println(this.getClass().getSimpleName() + ": Fetching constant \"" + expression.getConstant().getValue() + "\"");
                }
                InterpreterVisitor.getInstance().setReturnSymbol(expression.getConstant());
                break;
            case ACCESS:
                expression.visitChild(new InterpreterDecider(), expression.getFirstOperand());
                TemplateSymbol template = (TemplateSymbol)InterpreterVisitor.getInstance().getReturnSymbol();

                //remember the field to find in the template
                String fieldName = ((TField) expression.getSecondOperand()).getFieldName();

                ArrayList<String> map = GlobalScope.getInstance().getTemplateMapTable().get(template.getType());         //get the arraylist with the chosen template's fields

                SymbolTableEntry fieldContent = template.getContent().get(map.indexOf(fieldName));   //find the field we need with the map and get the content of the field

                if(this.verbose){
                    System.out.println(this.getClass().getSimpleName() + ": Accessing template with \"" + fieldName + "\".");
                }
                InterpreterVisitor.getInstance().setReturnSymbol(fieldContent);
                break;
            case INDEX:
                expression.visitChild(new InterpreterDecider(), expression.getFirstOperand());
                ArraySymbol currentArray = ((ArraySymbol)InterpreterVisitor.getInstance().getReturnSymbol());
                ArrayList<SymbolTableEntry> content = currentArray.getContent();
                expression.visitChild(new InterpreterDecider(), expression.getSecondOperand());
                Integer index = ((IntegerSymbol)InterpreterVisitor.getInstance().getReturnSymbol()).getValue();

                // We must allocate more entries in the array if we access an index out of bounds.
                Integer elementsToAllocate = index - content.size();
                if(elementsToAllocate >= 0){
                    for (int i = 0; i <= elementsToAllocate; i++) {
                        /*
                            This version of allocating elements allows expanding level 0 arrays with or without values assigned to them
                            (except for empty arrays - those are still illegal)
                            Example without this version:
                                Card c1 IS NEW Card{};
                                Card[] deck IS [c1];
                            Example with this version:
                                Card[] deck;
                        */
                        String complexType = "Primitive";
                        if(!(currentArray.getType().equals("Integer") ||
                             currentArray.getType().equals("Boolean") ||
                             currentArray.getType().equals("String"))){
                            // If the type of the array is not any of the primitives, it can only be an array of templates.
                            complexType = "Template";
                        }
                        content.add(
                            SymbolTableEntry.instantiateDefault(
                                currentArray.getType(),
                                complexType,
                                currentArray.getNestingLevel() - 1
                            ));


                        // content.add(
                        //     SymbolTableEntry.instantiateDefault(
                        //         currentArray.getContent().getFirst().getType(),
                        //         currentArray.getContent().getFirst().getComplexType(),
                        //         currentArray.getNestingLevel() - 1
                        //     ));
                    }
                }
                // We should only index after we have expanded the array.
                InterpreterVisitor.getInstance().setReturnSymbol(content.get(index));

                if(this.verbose){
                    System.out.println(this.getClass().getSimpleName() + ": Indexing array with \"" + index + "\".");
                }
                break;
            case CONCAT:
                //the type checker has already set up typecasting for us, so all inputs here will be strings
                expression.visitChild(new InterpreterDecider(), expression.getFirstOperand());
                String stringOne = ((StringSymbol)InterpreterVisitor.getInstance().getReturnSymbol()).getValue();
                expression.visitChild(new InterpreterDecider(), expression.getSecondOperand());
                String stringTwo = ((StringSymbol)InterpreterVisitor.getInstance().getReturnSymbol()).getValue();

                InterpreterVisitor.getInstance().setReturnSymbol(new StringSymbol(stringOne + stringTwo));
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private Integer[] getIntegerOperands(Expression node){
        Integer[] operands = new Integer[2];
        node.visitChild(new InterpreterDecider(), node.getFirstOperand());
        operands[0] = ((PrimitiveSymbol<Integer>)InterpreterVisitor.getInstance().getReturnSymbol()).getValue();
        node.visitChild(new InterpreterDecider(), node.getSecondOperand());
        operands[1] = ((PrimitiveSymbol<Integer>)InterpreterVisitor.getInstance().getReturnSymbol()).getValue();
        return operands;
    }
}
