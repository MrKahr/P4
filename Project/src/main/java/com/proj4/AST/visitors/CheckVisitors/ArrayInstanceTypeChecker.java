package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ArrayInstance;
import com.proj4.AST.nodes.Expression;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.MismatchedTypeException;
import com.proj4.exceptions.UndefinedTypeException;
import com.proj4.symbolTable.Scope;
import com.proj4.symbolTable.symbols.*;

public class ArrayInstanceTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        ArrayInstance arrayInstance = (ArrayInstance) node;
        arrayInstance.inheritScope();

        // Case 1
        if(!(arrayInstance.getChildren().size() > 0)){
            throw new UndefinedTypeException("Empty array must not be assigned to declared array");
        }

        //typecheck first child to create a precedent
        arrayInstance.visitChild(new CheckDecider(), 0);
        String expectedType = TypeCheckVisitor.getFoundType();
        String expectedComplexType = TypeCheckVisitor.getFoundComplexType();
        //typecheck every child
        for (int i = 1; i < arrayInstance.getChildren().size(); i++) {
            arrayInstance.visitChild(new CheckDecider(), arrayInstance.getChildren().get(i));
            if (!TypeCheckVisitor.getFoundType().equals(expectedType)) {
                throw new MismatchedTypeException("Array element does not match expected array type! Found \"" + TypeCheckVisitor.getFoundType() + "\" at index " + i + ". Expected \"" + expectedType + "\"."); 
            }
            if (!(TypeCheckVisitor.getFoundComplexType().equals(expectedComplexType))){
                throw new MismatchedTypeException("Array element does not match expected complex type! Found \"" + TypeCheckVisitor.getFoundType() + "\" at index " + i + ". Expected \"" + expectedComplexType + "\".");
            }
        }
        //after passing the check, create instance


        TypeCheckVisitor.setFoundType(expectedType, "Array");
    }
}