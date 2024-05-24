package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.MismatchedTypeException;
import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Assignment;

public class AssignmentTypeChecker implements NodeVisitor {

    public void visit(AST node) {
        Assignment assignment = (Assignment) node;

        assignment.visitChild(new CheckDecider(), assignment.getSymbolExpression()); // check the symbol to overwrite

        String expectedType = TypeCheckVisitor.getInstance().getFoundType(); // This evaluates arrays correct

        String expectedComplexType = TypeCheckVisitor.getInstance().getFoundComplexType();

        Integer expectedNestingLevel = TypeCheckVisitor.getInstance().getNestingLevel();

        assignment.visitChild(new CheckDecider(), assignment.getValueExpression()); // check the value to overwrite with

        String valueType = TypeCheckVisitor.getInstance().getFoundType();

        String valueComplexType = TypeCheckVisitor.getInstance().getFoundComplexType();

        Integer valueNestingLevel = TypeCheckVisitor.getInstance().getNestingLevel();

        // Nestinglevel
        if (expectedNestingLevel != valueNestingLevel){
            throw new MismatchedTypeException("Cannot assign value of complex type \"" + valueComplexType + "\"" + " with nestinglevel \"" + valueNestingLevel
                    + "\" to variable of complex type \"" + expectedComplexType + "\" with nestinglevel \"" + expectedNestingLevel + "\"!");
        }
        // Primitive types
        if (!expectedType.equals(valueType) || valueType.equals("Null")) {
            throw new MismatchedTypeException("Cannot assign value of type \"" + valueType
                    + "\" to variable of type \"" + expectedType + "\"!");
        }

        // Arrays should be the only case where we'd use complex type, so we can use nesting level instead of this
        // Complex types
        // if (!expectedComplexType.equals(valueComplexType) || valueComplexType.equals("Null")) {
        //     throw new MismatchedTypeException("Cannot assign value of complex type \"" + valueComplexType
        //             + "\" to variable of type \"" + expectedComplexType + "\"!");
        // }
    }
}