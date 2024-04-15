package com.proj4.AST.nodes;
//An enumeration to represent the operations that can be performed by a StringExp i.e. a string expression node
public enum StringExpOperator {
    CONCAT,     //  concatenate the string returned by the first child node with the string returned by the second
    LITERAL,    //  print the string as it is stored in the expression's "constant"-field
    VARIABLE    //  return the value bound to the identifier in the "identifier"-field as a string
}
