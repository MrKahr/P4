package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ArrayAccess;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.MismatchedTypeException;
import com.proj4.exceptions.UndefinedArrayExpection;
import com.proj4.symbolTable.symbols.ComplexSymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

public class ArrayAccessTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        ArrayAccess arrayAccess = (ArrayAccess) node;
        arrayAccess.inheritScope();

        // Case 1: array is not defined in scope 
        if(!arrayAccess.getScope().getVTable().containsKey(arrayAccess.getIdentifier())){
            throw new UndefinedArrayExpection("Array accessed in not defined in current scope");
        }

    
        // Case 2: Variable accessed is not an array 
        SymbolTableEntry possibleArray = arrayAccess.getScope().getVTable().get(arrayAccess.getIdentifier());
        try {
            ComplexSymbol array = (ComplexSymbol) possibleArray;
        } catch (Exception e) {
            throw new MismatchedTypeException(arrayAccess.getIdentifier() + " is not an array!");
        }
        if (true) {
            
        }
        
        // Case 3: Array is out of bounds 

        // Case 4: Index is not correct format 
    }
}
