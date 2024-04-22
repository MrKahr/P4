package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ArrayDecl;
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
        ComplexSymbol array = new ComplexSymbol();

        String expectedType = arrayDecl.getType();

        for (AST child : arrayDecl.getChildren()) {
            arrayDecl.visitChild(new CheckDecider(), child);
            if (!TypeCheckVisitor.getFoundType().equals(expectedType)) {
                throw new MismatchedTypeException();
            } else {
                //if the child has the right type, create an instance of it in the array
                switch (expectedType) {
                    case "Integer":
                        array.addContent(new IntSymbol(0)); //placeholder value to prevent ambiguity    (we'll know it when we interpret)
                        break;
                    case "Boolean":
                        array.addContent(new BooleanSymbol(false)); //placeholder value to prevent ambiguity
                        break;
                    case "String":
                    array.addContent(new StringSymbol("")); //placeholder value to prevent ambiguity
                        break;
                    default:    //not a primitive. Attempt to create a template from a blueprint
                        ComplexSymbol blueprint = Scope.getBTable().get(expectedType);
                        if (blueprint == null) {
                            throw new UndefinedTypeException("Value assigned to " + arrayDecl.getIdentifier() + " is not a primitive type!");
                        }
                        array.addContent(new ComplexSymbol(blueprint));
                }
            }
        }
        //TODO: actual values of the arrayDecl's children should be placed in the instances created in the for-loop
        arrayDecl.synthesizeScope();
    }
}