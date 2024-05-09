package com.proj4.symbolTable.symbols;

import com.proj4.exceptions.UnexpectedTypeException;

public abstract class PrimitiveSymbol<T> extends SymbolTableEntry{
    //Field
    private T value;
    //Method
    public T getValue(){
        return value;
    }

    public void setValue(T value){
        this.value = value;
    }

    public String getComplexType(){
        return "Primitive";
    }

    @Override
    public boolean equals(Object other){
        if (other == this) {
            return true;
        }

        if (!(other instanceof IntegerSymbol || other instanceof BooleanSymbol || other instanceof StringSymbol)) {
            return false;
        }

        if(!((SymbolTableEntry)other).getType().equals(this.getType())){
            return false;
        }

        switch (this.getType()) {
            case "Integer":
                return this.getValue().equals(((IntegerSymbol)other).getValue());
            case "Boolean":
                return this.getValue().equals(((BooleanSymbol)other).getValue());
            case "String":
                return this.getValue().equals(((StringSymbol)other).getValue());
            default:
                throw new UnexpectedTypeException("Expected a primitive type. Recieved " + this.getType());
        }
    }
}

//an attempt at onelining it
//return this.getValue().equals(((PrimitiveSymbol<T>)other).getValue());