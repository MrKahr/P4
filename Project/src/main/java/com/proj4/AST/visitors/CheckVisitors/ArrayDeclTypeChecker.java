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

        String arrayType = arrayDecl.getType();
        ArraySymbol array = new ArraySymbol(arrayType);

        //typecheck every child
        for (AST child : arrayDecl.getChildren()) {
            arrayDecl.visitChild(new CheckDecider(), child);
            if (!TypeCheckVisitor.getFoundType().equals(arrayType)) {
                throw new MismatchedTypeException();
            }
            //after passing the check, create a default instance
            SymbolTableEntry.instantiateDefault(arrayType); //TODO: need a complexType here!
        }
        
        TypeCheckVisitor.setFoundType(arrayDecl.getType(), "Array");
        arrayDecl.getScope().declareVariable(arrayDecl.getType(), array);
        arrayDecl.synthesizeScope();
    }
}