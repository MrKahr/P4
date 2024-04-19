package com.proj4.AST.visitors;

public abstract class TypeCheckVisitor implements NodeVisitor {

    //Field
    private static String foundType;    //this holds the most recent type we have found. Used to circumvent visitors not being able to return anything

    //Method
    public static String getFoundType(){
        return foundType;
    }

    public static void setFoundType(String type){
        foundType = type;
    }
}
