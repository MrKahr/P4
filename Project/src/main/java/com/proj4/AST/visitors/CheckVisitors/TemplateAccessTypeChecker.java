package com.proj4.AST.visitors.CheckVisitors;

import java.util.ArrayList;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.TemplateAccess;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.symbolTable.Scope;
import com.proj4.symbolTable.symbols.ComplexSymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

public class TemplateAccessTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        TemplateAccess templateAccess = (TemplateAccess) node;
        templateAccess.inheritScope();

        //proof of concept: indexing a field in a template:
        Scope scope = templateAccess.getScope();
        String identifier = templateAccess.getIdentifier();
        ArrayList<String> map = scope.getTTable().get(identifier);  //get the arraylist with the chosen template's fields
        ArrayList<String> path = templateAccess.getPath();  //get the path to take through the template
        ComplexSymbol subject = (ComplexSymbol) scope.getVTable().get(identifier);  //get the template we must index from the variable table

        String type = null; //the type to return starts as null

        for (String field : path) {
            int index = map.indexOf(field); //find the field we need with the map
            SymbolTableEntry newSubject = subject.getContent().get(index);  //get the entry stored in that field
            type = newSubject.getType();
            if (!type.equals("Integer") && !type.equals("Boolean") && !type.equals("String")) { 
                subject = (ComplexSymbol) newSubject;   //if the type of that field isn't primitive, prepare to index another ComplexSymbol
                map = scope.getTTable().get(type);      //get a map for the new ComplexSymbol
            }
        }
        TypeCheckVisitor.setFoundType(type);
    }
}
//we might be able to build this proof-of-concept thing into the variable-typechecker, but that could get out of hand pretty badly. Plus, it doesn't support the path-arrayList