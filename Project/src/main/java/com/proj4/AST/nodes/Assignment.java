package com.proj4.AST.nodes;

//the difference between PrimitiveDecl and Assignment is that with assignments
//we already know the type of the variable pointed to by the identifier
public class Assignment extends Statement{

    //Constructor
    public Assignment(){}
    
    public Expression getSymbolExpression(){    //the expression that gives us the symbol whose value should be overwritten
        return (Expression) getChildren().get(0);
    }

    public Expression getValueExpression(){     //the expression that gives us the value to overwrite with
        return (Expression) getChildren().get(1);
    }

    }
