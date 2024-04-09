package com.proj4.symbolTable;

import org.antlr.v4.runtime.RuleContext;

import com.proj4.exceptions.*;

//this class performs type checking on expressions
public class StringExpressionChecker implements TypeChecker{
    public void check(RuleContext ctx, Scope scope) throws UndefinedTypeException, MismatchedTypeException{
        //step 1: what type of expression is this?
        //step 2: select and perform the appropriate check
        //step 3: throw an error if the check fails
        
    }
}