package com.proj4.AST.visitors.CheckVisitors;

import java.util.ArrayList;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Declaration;
import com.proj4.AST.nodes.TemplateDecl;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.VariableAlreadyDefinedException;
import com.proj4.symbolTable.GlobalScope;
import com.proj4.symbolTable.ScopeManager;
import com.proj4.symbolTable.symbols.*;

public class TemplateDeclTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        TemplateDecl templateDecl = (TemplateDecl) node;

        ScopeManager.getInstance().enter();

        //the blueprint
        TemplateSymbol blueprint = new TemplateSymbol();
        blueprint.setType(templateDecl.getIdentifier());

        //the map
        ArrayList<String> map = new ArrayList<>();

        //note: template must not have identifier that is equal to "Integer", "Boolean", or "String" since those are reserved primitive types
        for (AST child : templateDecl.getChildren()) {
            SymbolTableEntry entry = null;

            Declaration identifiableChild = (Declaration) child; //will throw a ClassCastException if the child does not extend Declaration

            String t = identifiableChild.getType();
            String ct = identifiableChild.getComplexType();
            int nl = identifiableChild.getNestingLevel();

            entry = SymbolTableEntry.instantiateDefault(t, ct, nl);

            map.add(identifiableChild.getIdentifier());
            blueprint.addContent(entry);

        }
        String identifier = templateDecl.getIdentifier();
        if (GlobalScope.getInstance().getTemplateMapTable().get(identifier) == null && GlobalScope.getInstance().getBlueprintTable().get(identifier) == null) {
            GlobalScope.getInstance().getTemplateMapTable().put(identifier, map);
            GlobalScope.getInstance().getBlueprintTable().put(identifier, blueprint);
        } else {
            throw new VariableAlreadyDefinedException("Template \"" + identifier + "\" is already defined!");
        }
        ScopeManager.getInstance().exit();
    }

    //Note: templateDecls do NOT synthesize their scope. They only modify the global BTable and TTable.
    //Thus, all declarations in the the templateDecl's VTable should be fields in that template
}
