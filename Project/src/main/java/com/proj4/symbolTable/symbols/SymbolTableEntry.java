package com.proj4.symbolTable.symbols;

import com.proj4.AST.nodes.Typed;
import com.proj4.exceptions.UndefinedTypeException;
import com.proj4.symbolTable.Scope;

public abstract class SymbolTableEntry implements Typed{
    //Field
    private String type;

    //Method
    public String getType(){
        return type;
    }

    public abstract String getComplexType();

    public static SymbolTableEntry instantiateDefault(String type, String complexType, int nestingLevel){
        // Debug printing
        //System.out.println("Attempting to instantiate default instance of T: " + type + ", CT: " + complexType + ", NL: " + nestingLevel + ".");
        switch (complexType) {
            case "Array":
                return new ArraySymbol(type, 0);
            case "Template":
                return new TemplateSymbol(Scope.getBlueprintTable().get(type));
            case "Primitive":
                return instantiateDefault(type);
            default:
                throw new UndefinedTypeException("The complex type \"" + complexType + "\" is undefined!");
        }
    }

    public static SymbolTableEntry instantiateDefault(String type){
        switch (type) {
            case "Integer":
                return new IntegerSymbol(0);
            case "Boolean":
                return new BooleanSymbol(false);
            case "String":
                return new StringSymbol("o");
            default:
                throw new UndefinedTypeException("The type \"" + type + "\" is not a primtive!");
        }
    }

    public void setType(String type){
        this.type = type;
    }
}