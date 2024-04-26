package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Declaration;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.VariableAlreadyDefinedException;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

public class DeclarationTypeChecker extends TypeCheckVisitor{

    public void visit(AST node){
        Declaration declaration = (Declaration) node;
        declaration.inheritScope();

        // Check variable does not exist in Dtable
        if (declaration.getScope().getDTable().contains(declaration.getIdentifier())) {
            throw new VariableAlreadyDefinedException();
        } 

        //create a new variable with the given type
        //TODO: Check instantiate default 
        declaration.getScope().declareVariable(declaration.getIdentifier(), SymbolTableEntry.instantiateDefault(declaration.getType(), declaration.getComplexType()));

        if(declaration.getChildren().size() > 0){ //if this declaration also assigns a value, check that the type is ok
            // Visit child
            // Child is an assignment
            declaration.visitChild(new CheckDecider(), declaration.getInitialAssignment());    //visit the child and set the recently found field of our checkdecider
            // Since we check if the type of the assignment is ok in the assignment-child, we won't need to check it here: in the declaration node.
        }

        // Set the found type to that of the declaration
        TypeCheckVisitor.setFoundType(declaration.getType(), declaration.getComplexType());

        // New variable is added to parent scope 
        declaration.synthesizeScope();
    }
}