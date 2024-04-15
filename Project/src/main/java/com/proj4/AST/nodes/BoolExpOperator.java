package com.proj4.AST.nodes;
//An enumeration to represent the operations that can be performed by a BoolExp i.e. a boolean expression node
public enum BoolExpOperator {
    LESS_THAN,              //  a<b
    LESS_OR_EQUALS,         //  a<=b
    GREATER_THAN,           //  a>b
    GREATER_OR_EQUALS,      //  a>=b
    EQUALS,                 //  a=b
    NOT_EQUALS,             //  a!=b
    OR,                     //  a||b
    AND,                    //  a&&b
    NOT                     //  !a
}