package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ArrayAccess;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.MismatchedTypeException;
import com.proj4.exceptions.UndefinedArrayExpection;
import com.proj4.symbolTable.symbols.ActionSymbol;

public class ArrayAccessTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        ArrayAccess arrayAccess = (ArrayAccess) node;
        arrayAccess.inheritScope();

        // Case 1: array is not defined in scope 
        if(!arrayAccess.getScope().getVTable().containsKey(arrayAccess.getIdentifier())){
            throw new UndefinedArrayExpection("Array accessed in not defined in current scope");
        }

        // Case 2: Array is not an array 
        ActionSymbol array = null;
        try {
            array = (ActionSymbol) arrayAccess.getScope().getVTable().get(arrayAccess.getIdentifier());
        } catch (ClassCastException cce) {
            throw new MismatchedTypeException("Identifier \"" + arrayAccess.getIdentifier() + "\" indexed as array but is not an array!");
        }

        // Case 3: Index is not an integer 
        arrayAccess.visitChild(new CheckDecider(), arrayAccess.getIndex());
        if(!TypeCheckVisitor.getFoundType().equals("Integer")){
            throw new MismatchedTypeException("Index for array \"" + arrayAccess.getIdentifier() + "\" is not integer!");
        }

        TypeCheckVisitor.setFoundType(array.getType(), array.getContentComplexType());
    }
}
