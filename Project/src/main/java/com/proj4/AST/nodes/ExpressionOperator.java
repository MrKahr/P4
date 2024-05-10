package com.proj4.AST.nodes;

public enum ExpressionOperator {
//RETURNS PRIMITIVE
    //RETURNS INTEGER
    ADD,        //  a+b
    SUBTRACT,   //  a-b
    DIVIDE,     //  a/b
    MULTIPLY,   //  a*b
    NEGATE,     //  -a
    //RETURNS BOOLEAN
    LESS_THAN,              //  a<b
    LESS_OR_EQUALS,         //  a<=b
    GREATER_THAN,           //  a>b
    GREATER_OR_EQUALS,      //  a>=b
    EQUALS,                 //  a=b
    NOT_EQUALS,             //  a!=b
    OR,                     //  a||b
    AND,                    //  a&&b
    NOT,                    //  !a
    //RETURNS STRING
    //LITERAL,    //  print the string as it is stored in the expression's "constant"-field <- obsolete as CONSTANT exists
    VARIABLE,   //  return the value bound to the identifier in the "identifier"-field as a string
//RETURNS ANY
    CONSTANT,   //  return what's in the "constant"-field
    INDEX,      //  a[b]    <- used to be ArrayAccess
    ACCESS,     //  a.b     <- used to be TemplateAccess
}
