package com.proj4.AST.visitors.InterpreterVisitors;


import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Assignment;
import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.symbolTable.symbols.*;

public class AssignmentInterpreter extends InterpreterVisitor {

    public void visit(AST node) {
        Assignment assignment = (Assignment) node;
        assignment.visitChild(new InterpreterDecider(), assignment.getSymbolExpression());
        SymbolTableEntry symbol = InterpreterVisitor.getReturnSymbol(); //save a pointer to the symbol
        assignment.visitChild(new InterpreterDecider(), assignment.getValueExpression());
        SymbolTableEntry value = InterpreterVisitor.getReturnSymbol();

        switch (value.getComplexType()) {
            case "Primitive":
                switch (value.getType()) {
                    case "Integer":
                        IntegerSymbol intValue = (IntegerSymbol) value;
                        ((IntegerSymbol) symbol).setValue(intValue.getValue());
                        System.out.println("Assigning value \"" + intValue.getValue() + "\".");
                        break;
                    case "Boolean":
                        BooleanSymbol boolValue = (BooleanSymbol) value;
                        ((BooleanSymbol) symbol).setValue(boolValue.getValue());
                        break;
                    case "String":
                        StringSymbol stringValue = (StringSymbol) value;
                        ((StringSymbol) symbol).setValue(stringValue.getValue());
                        break;
                    default:
                        throw new RuntimeException("Interpreter failed. Read invalid primitive type \"" + value.getType() + "\"!");
                }
                break;
            case "Template":
                TemplateSymbol templateValue = (TemplateSymbol) value;
                ((TemplateSymbol) symbol).setContent(templateValue.getContent());
                break;

            case "Array":
                ArraySymbol arrayValue = (ArraySymbol) value;
                ((ArraySymbol) symbol).setContent(arrayValue.getContent());
            default:
                throw new RuntimeException("Interpreter failed. Read invalid complex type \"" + value.getComplexType() + "\"!");
        }

    }
}
