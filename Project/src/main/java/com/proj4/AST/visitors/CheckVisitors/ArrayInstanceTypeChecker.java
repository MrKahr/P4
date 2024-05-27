package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ArrayInstance;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.MismatchedTypeException;
import com.proj4.exceptions.UndefinedTypeException;

public class ArrayInstanceTypeChecker implements NodeVisitor{

    public void visit(AST node){
        ArrayInstance arrayInstance = (ArrayInstance) node;

        // Case 1: Array is empty!
        if(!(arrayInstance.getChildren().size() > 0)){
            throw new UndefinedTypeException("Empty array must not be assigned to declared array");
        }

        // Typecheck first child to create find the expected types for all children since all elements in an array are assumed to be identical
        arrayInstance.visitChild(new CheckDecider(), 0);
        String expectedType = TypeCheckVisitor.getInstance().getFoundType();
        String expectedComplexType = TypeCheckVisitor.getInstance().getFoundComplexType();
        Integer expectedNestingLevel = TypeCheckVisitor.getInstance().getNestingLevel();

        //System.out.println(this.getClass().getSimpleName() + ": DEBUG ARRAY TYPE = " + expectedType);
        // Typecheck every child
        for (int i = 1; i < arrayInstance.getChildren().size(); i++) {
            arrayInstance.visitChild(new CheckDecider(), arrayInstance.getChildren().get(i));

            if (!TypeCheckVisitor.getInstance().getFoundType().equals(expectedType)) {
                throw new MismatchedTypeException("Array element does not match expected array type! Found \"" + TypeCheckVisitor.getInstance().getFoundType() + "\" at index " + i + ". Expected \"" + expectedType + "\".");
            }
            if (!(TypeCheckVisitor.getInstance().getFoundComplexType().equals(expectedComplexType))){
                throw new MismatchedTypeException("Array element does not match expected complex type! Found \"" + TypeCheckVisitor.getInstance().getFoundType() + "\" at index " + i + ". Expected \"" + expectedComplexType + "\".");
            }
            if(expectedNestingLevel != TypeCheckVisitor.getInstance().getNestingLevel()){
                throw new MismatchedTypeException("Nesting level mismatch between array parent and child elements. Expected " + expectedNestingLevel + ", got " + (TypeCheckVisitor.getInstance().getNestingLevel()));
            }
        }
            // Check whether child is an array, if so increment nesting level
            if(TypeCheckVisitor.getInstance().getFoundComplexType().equals("Array")){
                TypeCheckVisitor.getInstance().setFoundType(expectedType, "Array", TypeCheckVisitor.getInstance().getNestingLevel() + 1);

            } else {
                TypeCheckVisitor.getInstance().setFoundType(expectedType, "Array", 0);
            }
    }
}