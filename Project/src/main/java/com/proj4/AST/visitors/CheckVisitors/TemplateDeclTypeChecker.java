package com.proj4.AST.visitors.CheckVisitors;

import java.util.ArrayList;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Declaration;
import com.proj4.AST.nodes.TemplateDecl;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.VariableAlreadyDefinedException;
import com.proj4.symbolTable.Scope;
import com.proj4.symbolTable.symbols.*;

public class TemplateDeclTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        TemplateDecl templateDecl = (TemplateDecl) node;

        //the blueprint
        TemplateSymbol blueprint = new TemplateSymbol();
        //the map
        ArrayList<String> map = new ArrayList<>();

        //note: template must not have identifier that is equal to "Integer", "Boolean", or "String" since those are reserved primitive types
        for (AST child : templateDecl.getChildren()) {
            SymbolTableEntry entry = null;

            Declaration identifiableChild = (Declaration) child; //will throw a ClassCastException if the child does not extend Declaration

            identifiableChild.getType();

            map.add(identifiableChild.getIdentifier());
            blueprint.addContent(entry);

        }
        String identifier = templateDecl.getIdentifier();
        if (Scope.getTTable().get(identifier) == null && Scope.getBTable().get(identifier) == null) {
            Scope.getTTable().put(identifier, map);
            Scope.getBTable().put(identifier, blueprint);
        } else {
            throw new VariableAlreadyDefinedException("Template \"" + identifier + "\" is already defined!");
        }
        // Add to blueprint 
        // Add to map

        //how to typecheck:

        //typecheck a child
        //add child as field in blueprint
        //repeat for all children

        //if child is another template
        //create an instance of that template as a field
    }

    //TODO: Note: templateDecls do NOT synthesize their scope. They only modify the global BTable and TTable.
    //TODO: Thus, all declarations in the the templateDecl's VTable should be fields in that template

 
    /*
            switch (child.getClass().getSimpleName()) {
                case "PrimitiveDecl":
                    //get the type, identifier, and value
                    //create instance of that type, mapped to that identifier
                        switch (TypeCheckVisitor.getFoundType()) {  //declarations don't change the foundType, so the type of their expression propagates up to us here
                            case "Integer":
                                entry = new IntSymbol(0);
                                break;
                            case "Boolean":
                                entry = new BooleanSymbol(false);
                                break;
                            case "String":
                                entry = new StringSymbol("");
                                break;
                            default:
                                throw new UndefinedTypeException("Primitive type \"" + TypeCheckVisitor.getFoundType() + "\" not defined!");
                        }
                    break;
                case "ArrayDecl":
                    //get the type, identifier, and value
                    //create instance of that type, mapped to that identifier
                    break;
                case "TemplateInstance":
                    //get the type and identifier
                    //create instance of that type, mapped to that identifier
                    break;
                default:
                    throw new UndefinedTypeException("Unrecognized declaration in template \"" + templateDecl.getIdentifier() + "\"!");
            }
     */
}
