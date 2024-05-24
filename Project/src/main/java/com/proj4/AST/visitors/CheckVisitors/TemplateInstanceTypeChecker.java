package com.proj4.AST.visitors.CheckVisitors;

import java.util.ArrayList;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.TemplateInstance;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.MismatchedTypeException;
import com.proj4.symbolTable.symbols.ArraySymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;
import com.proj4.symbolTable.symbols.TemplateSymbol;
import com.proj4.symbolTable.GlobalScope;

public class TemplateInstanceTypeChecker implements NodeVisitor{

    public void visit(AST node){
        TemplateInstance templateInstance = (TemplateInstance) node;

        String templateType = templateInstance.getType();
        //we make assignments to the fields in the order the fields are declared
        if (templateInstance.getChildren().size() > 0) {
            //get the blueprint for comparing types
            TemplateSymbol blueprint = GlobalScope.getInstance().getBlueprintTable().get(templateInstance.getType());
            ArrayList<SymbolTableEntry> content = blueprint.getContent();

            for (int index = 0; index < templateInstance.getChildren().size(); index++) {
                templateInstance.visitChild(new CheckDecider(), index);
                SymbolTableEntry entry = content.get(index);

                if (!TypeCheckVisitor.getInstance().getFoundType().equals(entry.getType())) {
                    throw new MismatchedTypeException("Mismatched type in template instatiation! Expected \"" + entry.getType() + "\"" + "but got \"" + TypeCheckVisitor.getInstance().getFoundType() + "\".");
                }

                if (!TypeCheckVisitor.getInstance().getFoundComplexType().equals(entry.getComplexType())) {
                    throw new MismatchedTypeException("Mismatched complex type in template instatiation! Expected \"" + entry.getComplexType() + "\"" + "but got \"" + TypeCheckVisitor.getInstance().getFoundComplexType() + "\".");
                }

                if (entry instanceof ArraySymbol) {
                    try {
                        if(!TypeCheckVisitor.getInstance().getNestingLevel().equals(((ArraySymbol) entry).getNestingLevel())){
                            throw new MismatchedTypeException("Mismatched nesting level in template instatiation! Expected \"" + ((ArraySymbol) entry).getNestingLevel() + "\"" + "but got \"" + TypeCheckVisitor.getInstance().getNestingLevel() + "\".");
                        };
                    } catch (ClassCastException e) {
                        throw new MismatchedTypeException("Mismatched complex type in template instatiation! Expected \"Array\"" + "but got \"" + TypeCheckVisitor.getInstance().getFoundComplexType() + "\".");
                    }
                }
            }
        }
        TypeCheckVisitor.getInstance().setFoundType(templateType, "Template", -1);
    }
}
