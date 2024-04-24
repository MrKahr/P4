package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.PrimitiveDecl;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.MismatchedTypeException;
import com.proj4.exceptions.UndefinedTypeException;
import com.proj4.exceptions.VariableAlreadyDefinedException;
import com.proj4.symbolTable.symbols.BooleanSymbol;
import com.proj4.symbolTable.symbols.IntSymbol;
import com.proj4.symbolTable.symbols.StringSymbol;

public class PrimitiveDeclTypeChecker extends TypeCheckVisitor{

    public void visit(AST node){
        PrimitiveDecl primitiveDecl = (PrimitiveDecl) node;
        primitiveDecl.inheritScope();

        // Check variable does not exist in Dtable
        if (primitiveDecl.getScope().getDTable().contains(primitiveDecl.getExpectedType())) {
            throw new VariableAlreadyDefinedException();
        } 
        
        if(primitiveDecl.getChildren().size() > 0){ //if this declaration also assigns a value, check that the type is ok
            // Visit child
            primitiveDecl.visitChild(new CheckDecider(), 0);    //visit the child and set the recently found field of our checkdecider
            
            // Check type is ok
            if (!primitiveDecl.getExpectedType().equals(TypeCheckVisitor.getFoundType())) {
                throw new MismatchedTypeException();
            }
        }

        String identifier = primitiveDecl.getIdentifier();
        switch (primitiveDecl.getExpectedType()) {  //at this point, we know the value is this type as well
            case "Integer":
                primitiveDecl.getScope().declareVariable(identifier, new IntSymbol(0)); //placeholder value to prevent ambiguity
                break;
            case "Boolean":
                primitiveDecl.getScope().declareVariable(identifier, new BooleanSymbol(false)); //placeholder value to prevent ambiguity
                break;
            case "String":
                primitiveDecl.getScope().declareVariable(identifier, new StringSymbol("")); //placeholder value to prevent ambiguity
                break;
            default:
                throw new UndefinedTypeException("Value assigned to " + identifier + " is not a primitive type!");
        }

        // New variable is added to parent scope 
        primitiveDecl.synthesizeScope();
    }
}