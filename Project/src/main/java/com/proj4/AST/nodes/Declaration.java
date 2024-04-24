package com.proj4.AST.nodes;

import com.proj4.exceptions.MalformedAstException;

public class Declaration extends Statement implements Identifiable, Typed{
    //Field
    private String identifier;  //the identifier to bind this variable to
    private String type;    //the type this variable should have
    private String complexType; //the complex type of this variable i.e. Primitive, Array, or Template

    //Constructor
    public Declaration(String identifier, String type, String complexType){
        this.identifier = identifier;
        this.type = type;
        this.complexType = complexType;
    }

    //Method
    public String getIdentifier(){
        return identifier;
    }

    public String getType(){
        return type;
    }

    public String getComplexType(){
        return complexType;
    }

    public Assignment getInitialAssignment(){
        try {
            return (Assignment) getChild(0);
        } catch (ClassCastException cce) {
            throw new MalformedAstException("Expected Assignment but got " + getChild(0).getClass().getSimpleName());
        }
    };
}