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

    public static SymbolTableEntry instantiateDefault(String type, String complexType){
        
                switch (complexType) {
                    case "Array":
                        return new ArraySymbol(type, complexType);
                    case "Template":
                        return new TemplateSymbol(Scope.getBTable().get(type));
                    case "Action":
                        return new ActionSymbol(type, complexType, null);
                    case "Primitive": 
                        return instantiateDefault(type);
                    default:
                        throw new UndefinedTypeException("The complex type \"" + complexType + "\" is undefined!");
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