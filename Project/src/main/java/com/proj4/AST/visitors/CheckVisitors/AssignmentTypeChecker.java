package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.MismatchedTypeException;
import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Assignment;

public class AssignmentTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        Assignment assignment = (Assignment) node;
        
        assignment.visitChild(new CheckDecider(), assignment.getSymbolExpression());    //check the symbol to overwrite
        
        String expectedType = TypeCheckVisitor.getFoundType();

        String expectedComplexType = TypeCheckVisitor.getFoundComplexType();

        assignment.visitChild(new CheckDecider(), assignment.getValueExpression());    //check the value to overwrite with
        
        if(!expectedType.equals(TypeCheckVisitor.getFoundType())){
            throw new MismatchedTypeException("Cannot assign value of type \"" + TypeCheckVisitor.getFoundType() + "\" to variable of type \"" + expectedType + "\"!");
        }
        if (!expectedComplexType.equals(TypeCheckVisitor.getFoundComplexType())) {
            throw new MismatchedTypeException("Cannot assign value of complex type \"" + TypeCheckVisitor.getFoundComplexType() + "\" to variable of type \"" + expectedComplexType + "\"!");
        }
    }
}