package com.proj4.AST.visitors.CheckVisitors;

import java.util.ArrayList;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.TemplateAccess;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.symbolTable.Scope;
import com.proj4.symbolTable.symbols.TemplateSymbol;
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
        TemplateSymbol subject = (TemplateSymbol) scope.getVTable().get(identifier);  //get the template we must index from the variable table

        String type = null; //the type to return starts as null

        for (String field : path) {
            int index = map.indexOf(field); //find the field we need with the map
            SymbolTableEntry newSubject = subject.getContent().get(index);  //get the entry stored in that field
            type = newSubject.getType();
            if (!type.equals("Integer") && !type.equals("Boolean") && !type.equals("String")) { 
                subject = (TemplateSymbol) newSubject;   //if the type of that field isn't primitive, prepare to index another TemplateSymbol
                map = scope.getTTable().get(type);      //get a map for the new TemplateSymbol
            }
        }
        TypeCheckVisitor.setFoundType(type);
    }
}