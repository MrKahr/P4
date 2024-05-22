package com.proj4.symbolTable.symbols;

import com.proj4.AST.nodes.Typed;
import com.proj4.exceptions.UndefinedTypeException;
import com.proj4.symbolTable.GlobalScope;

public abstract class SymbolTableEntry implements Typed{
    //Field
    private String type;

    //Method
    public String getType(){
        return type;
    }

    public abstract String getComplexType();

    public static SymbolTableEntry instantiateDefault(String type, String complexType, int nestingLevel){
        switch (complexType) {
            case "Array":
                return new ArraySymbol(type, nestingLevel);
            case "Template":
                return new TemplateSymbol(GlobalScope.getInstance().getBlueprintTable().get(type));
            case "Primitive":
                return instantiateDefault(type);
            case "Null":
                return instantiateDefault(complexType);
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
                return new StringSymbol("");
            case "Null":
                return new NullSymbol("Null");
            default:
                throw new UndefinedTypeException("The type \"" + type + "\" is not a primitive!");
        }
    }

    public void setType(String type){
        this.type = type;
    }
}