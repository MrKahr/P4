package com.proj4.AST.visitors;

//this class is a singleton
public class TypeCheckVisitor {

    //Field
    private static TypeCheckVisitor instance;

    private String foundType;        //this holds the most recent type we have found. Used to circumvent visitors not being able to return anything
    private String foundComplexType; //this holds the most recent complex type we have found. Used for differentiating between arrays, templates, and primitives
    private String currentAction;    //if we're inside an action body, this will hold the action's name
    private Integer nestingLevel = -1;

    //Constructor
    private TypeCheckVisitor(){

    }

    //Method
    public static TypeCheckVisitor getInstance(){
        if (instance == null) {
            instance = new TypeCheckVisitor();
        }
        return instance;
    }

    public String getFoundType(){
        return foundType;
    }

    public String getFoundComplexType(){
        return foundComplexType;
    }

    public String getCurrentAction(){
        return currentAction;
    }

    public Integer getNestingLevel(){
        return nestingLevel;
    }

    public void setFoundType(String type, String complexType, int nesting){
        foundType = type;
        foundComplexType = complexType;
        nestingLevel = nesting;
        // Debug printing
        //System.out.println("TypeCheckVisitor: setFoundType(Type = "+type+" | Complex type = "+complexType+" | Nesting Level = "+nesting+")");
    }

    public void setCurrentAction(String actionName){
        currentAction = actionName;
    }

    public void setNestingLevel(Integer level){
        //System.out.println("TypeCheckVisitor: DEBUG Setting nesting level = " + level);
        this.nestingLevel = level;
    }

}
