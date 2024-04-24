package com.proj4.AST.nodes;
//An enumeration to represent the operations that can be performed by a MathExp i.e. a math expression node
public enum MathExpOperator {
    ADD,        //  a+b
    SUBTRACT,   //  a-b
    DIVIDE,     //  a/b
    MULTIPLY,   //  a*b
    NEGATE,     //  -a
    CONSTANT,   //  return what's in the "constant"-field
}
