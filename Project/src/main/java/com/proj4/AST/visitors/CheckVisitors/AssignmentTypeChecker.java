package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.MismatchedTypeException;
import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Assignment;

public class AssignmentTypeChecker extends TypeCheckVisitor{
    
    public void visit(AST node){
        Assignment assignment = (Assignment) node;
        assignment.inheritScope();

        String expectedType = assignment.getScope().getVTable().get(assignment.getIdentifier()).getType();
        assignment.visitChild(new CheckDecider(), assignment.getNewValue());

        if(!expectedType.equals(TypeCheckVisitor.getFoundType())){
            throw new MismatchedTypeException();
        }
    }
}