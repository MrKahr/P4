package com.proj4.symbolTable.symbols;

import com.proj4.exceptions.UndefinedTypeException;
import com.proj4.symbolTable.Scope;

public abstract class SymbolTableEntry{
    //Field
    private String type;

    //Method
    public String getType(){
        return type;
    }

    public static SymbolTableEntry instantiateDefault(String type, String complexType){
        switch (type) {
            case "Integer":
                return new IntSymbol(0);
            case "Boolean":
                return new BooleanSymbol(false);
            case "String":
                return new StringSymbol("");
            default:
                switch (complexType) {
                    case "Array":
                        return new ArraySymbol(type);
                    case "Template":
                        return new TemplateSymbol(Scope.getBTable().get(type));
                    default:
                        throw new UndefinedTypeException("The type \"" + type + "\" is undefined and not a primtive!");
                }
        }
    }
    
    public static SymbolTableEntry instantiateDefault(String type){
        switch (type) {
            case "Integer":
                return new IntSymbol(0);
            case "Boolean":
                return new BooleanSymbol(false);
            case "String":
                return new StringSymbol("");
            default:
                throw new UndefinedTypeException("The type \"" + type + "\" is not a primtive!");
        }
    }

    
    public void setType(String type){
        this.type = type;
    }
}