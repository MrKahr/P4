package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.IfElse;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.TypeCheckVisitor;
import com.proj4.exceptions.MismatchedTypeException;

// Assumptions: Condition,  Two bodies, if body, else body 
public class IfElseTypeChecker extends TypeCheckVisitor{

    public void visit(AST node){
        IfElse ifElse = (IfElse) node;

        // Get the current type of the condition from the check decider 
        ifElse.visitChild(new CheckDecider(), ifElse.getCondition());

        if(!TypeCheckVisitor.getFoundType().equals("Boolean")){
            throw new MismatchedTypeException("Error in condition expression for if statement. Expected: Boolean. Recieved " + TypeCheckVisitor.getFoundType());
        }
        // Check each body 
        ifElse.visitChild(new CheckDecider(), ifElse.getThenBlock());
        if (ifElse.getElseBlock() != null) {
            ifElse.visitChild(new CheckDecider(), ifElse.getElseBlock());
        }
    }
}
