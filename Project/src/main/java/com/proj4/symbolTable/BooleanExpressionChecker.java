package com.proj4.symbolTable;

import org.antlr.v4.runtime.RuleContext;
import com.proj4.antlrClass.DBLLexer;
import com.proj4.antlrClass.DBLParser;
import com.proj4.exceptions.*;

//this class performs type checking on expressions
public class BooleanExpressionChecker implements TypeChecker {
    public void check(RuleContext ctx, Scope scope) throws UndefinedTypeException, MismatchedTypeException {
        // step 1: what type of expression is this?
        // step 2: select and perform the appropriate check
        // step 3: throw an error if the check fails
        DBLParser.BoolExprContext boolctx = (DBLParser.BoolExprContext) ctx;
        if (boolctx.GT() != null) {

        } else if (boolctx.GTOE() != null) {

        } else if (boolctx.NOT() != null && boolctx.EQUALS() != null) {

        }

        if(boolctx.expr() != null) {

        } else if(boolctx.boolExpr() != null) {
            
        }

    }
}