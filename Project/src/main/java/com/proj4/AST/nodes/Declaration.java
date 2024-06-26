package com.proj4.AST.nodes;

import com.proj4.exceptions.MalformedAstException;

public class Declaration extends Statement implements Identifiable, Typed{
    //Field
    private String identifier;  //the identifier to bind this variable to
    private String type;    //the type this variable should have
    private String complexType; //the complex type of this variable i.e. Primitive, Array, or Template
    private int nestingLevel;

    //Constructor
    public Declaration(String identifier, String type, String complexType){
        this.identifier = identifier;
        this.type = type;
        this.complexType = complexType;
        this.nestingLevel = -1; // Means no nesting level
    }

    public Declaration(String identifier, String type, String complexType, int nestingLevel){
        this.identifier = identifier;
        this.type = type;
        this.complexType = complexType;
        this.nestingLevel = nestingLevel;
    }

    //Method
    public String getIdentifier(){
        if (identifier == null) {
            throw new MalformedAstException("Null identifier passed to declaration - cannot declare variable in symbol table.");
        }
        return identifier;
    }

    public String getType(){
        if(type == null) {
            throw new MalformedAstException("Null type passed to declaration - cannot declare variable in symbol table.");
        }
        return type;
    }

    public String getComplexType(){
        if(type == null){
            throw new MalformedAstException("Null complex type passed to declaration - cannot declare variable in symbol table.");
        }
        return complexType;
    }

    public int getNestingLevel(){
        return nestingLevel;
    }

    public Assignment getInitialAssignment(){
        try {
            return (Assignment) getChild(0);
        } catch (ClassCastException cce) {
            throw new MalformedAstException("Expected Assignment but got " + getChild(0).getClass().getSimpleName());
        }
    };
}