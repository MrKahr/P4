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

                String entryType = entry.getType();
                String entryComplexType = entry.getComplexType();

                String foundType = TypeCheckVisitor.getInstance().getFoundType();
                String foundComplexType = TypeCheckVisitor.getInstance().getFoundComplexType();
                Integer foundNestingLevel = TypeCheckVisitor.getInstance().getNestingLevel();

                if (!foundType.equals(entry.getType())) {
                    throw new MismatchedTypeException("Mismatched type in template instantiation! Expected \"" + entryType + "\" " + "but got \"" + foundType + "\".");
                }

                // This type should also account for the case where we use array index.
                // In such case foundType is e.g. String (matching entryType) but complex type is Array (EXPLOSION!!!)
                // Thus, we will allow a complex type of array ONLY if foundNestingLevel <= 0
                if (!foundComplexType.equals(entry.getComplexType()) && foundNestingLevel > 0) {
                    throw new MismatchedTypeException("Mismatched complex type in template instantiation! Expected \"" + entryComplexType + "\"" + " but got \"" + foundComplexType + "\" with nesting level " + foundNestingLevel);
                }

                if (entry instanceof ArraySymbol) {
                    try {
                        if(!foundNestingLevel.equals(((ArraySymbol) entry).getNestingLevel())){
                            throw new MismatchedTypeException("Mismatched nesting level in template instantiation! Expected \"" + ((ArraySymbol) entry).getNestingLevel() + "\"" + " but got \"" + foundNestingLevel + "\".");
                        };
                    } catch (ClassCastException e) {
                        throw new MismatchedTypeException("Mismatched complex type in template instantiation! Expected \"Array\"" + " but got \"" + foundComplexType + "\".");
                    }
                }
            }
        }
        TypeCheckVisitor.getInstance().setFoundType(templateType, "Template", -1);
    }
}
