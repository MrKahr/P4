package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.MismatchedTypeException;
import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Assignment;

public class AssignmentTypeChecker implements NodeVisitor{
    
    public void visit(AST node){
        Assignment assignment = (Assignment) node;
        
        assignment.visitChild(new CheckDecider(), assignment.getSymbolExpression());    //check the symbol to overwrite
        
        String expectedType = TypeCheckVisitor.getInstance().getFoundType();

        String expectedComplexType = TypeCheckVisitor.getInstance().getFoundComplexType();

        assignment.visitChild(new CheckDecider(), assignment.getValueExpression());    //check the value to overwrite with
        
        String valueType = TypeCheckVisitor.getInstance().getFoundType();

        String valueComplexType = TypeCheckVisitor.getInstance().getFoundComplexType();

        if(!expectedType.equals(valueType)){
            throw new MismatchedTypeException("Cannot assign value of type \"" + TypeCheckVisitor.getInstance().getFoundType() + "\" to variable of type \"" + expectedType + "\"!");
        }
        if (!expectedComplexType.equals(valueComplexType)) {
            throw new MismatchedTypeException("Cannot assign value of complex type \"" + TypeCheckVisitor.getInstance().getFoundComplexType() + "\" to variable of type \"" + expectedComplexType + "\"!");
        }
    }
}