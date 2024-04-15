package com.proj4.symbolTable;

import org.antlr.v4.runtime.RuleContext;

import com.proj4.exceptions.*;

//this class performs type checking on action declarations
public class ActionChecker implements TypeChecker{
    public void check(RuleContext ctx, Scope scope) throws UndefinedTypeException, MismatchedTypeException{
        
    }
}