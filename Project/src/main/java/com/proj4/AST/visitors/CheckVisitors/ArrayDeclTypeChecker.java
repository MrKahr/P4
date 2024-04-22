package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ArrayDecl;
import com.proj4.AST.nodes.Expression;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.MismatchedTypeException;
import com.proj4.exceptions.UndefinedTypeException;
import com.proj4.symbolTable.Scope;
import com.proj4.symbolTable.symbols.*;

public class ArrayDeclTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        ArrayDecl arrayDecl = (ArrayDecl) node;
        arrayDecl.inheritScope();
        //we know from this being an ArrayDecl-node that this is an array

        String arrayType = arrayDecl.getType();

        for (AST child : arrayDecl.getChildren()) {
            arrayDecl.visitChild(new CheckDecider(), child);
            if (!TypeCheckVisitor.getFoundType().equals(arrayType)) {
                throw new MismatchedTypeException();
            }
        }
        instantiateArray(arrayDecl);

        arrayDecl.synthesizeScope();
    }

    public void instantiateArray(ArrayDecl arrayDecl){
        ArraySymbol array = new ArraySymbol(arrayDecl.getType());
        //if the child has the right type, create an instance of it in the array
        switch (arrayDecl.getType()) {
            case "Integer":
                for (AST child : arrayDecl.getChildren()) {
                    array.addContent(new IntSymbol(0)); //placeholder value to prevent ambiguity    (we'll know it when we interpret)
                }
                break;
            case "Boolean":
            for (AST child : arrayDecl.getChildren()) {
                    array.addContent(new BooleanSymbol(false)); //placeholder value to prevent ambiguity
                }
                break;
            case "String":
                for (AST child : arrayDecl.getChildren()) {
                    array.addContent(new StringSymbol("")); //placeholder value to prevent ambiguity
                }
                break;
            default:    //not a primitive. Attempt to create a template from a blueprint or an array
            //TODO: the actual values of the arrayDecl's children should be placed in the instances created
            //TODO: without the actual values, we can't create proper subarrays or templates in the array
        }

        arrayDecl.getScope().declareVariable(arrayDecl.getType(), array);
    }
}