package com.proj4.AST.visitors;

public abstract class TypeCheckVisitor implements NodeVisitor {

    //Field
    private static String foundType;    //this holds the most recent type we have found. Used to circumvent visitors not being able to return anything
    private static String foundComplexType;  //this holds the most recent complex type we have found. Used for differentiating between arrays, templates, and primitives
    private static String currentAction;//if we're inside an action body, this will hold the action's name
    private static Integer nestingLevel = 0;

    //Method
    public static String getFoundType(){
        return foundType;
    }

    public static String getFoundComplexType(){
        return foundComplexType;
    }

    public static String getCurrentAction(){
        return currentAction;
    }

    public static Integer getNestingLevel(){
        return nestingLevel;
    }

    public static void setFoundType(String type, String complexType, int nesting){
        foundType = type;
        foundComplexType = complexType;
        nestingLevel = nesting;
    }
    
    public static void setCurrentAction(String actionName){
        currentAction = actionName;
    }

    public static void setNestingLevel(Integer level){
       nestingLevel = level;
    }

}
