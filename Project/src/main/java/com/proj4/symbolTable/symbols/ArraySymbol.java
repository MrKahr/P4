package com.proj4.symbolTable.symbols;

import java.util.ArrayList;

import com.proj4.exceptions.UndefinedTypeException;

public class ArraySymbol extends SymbolTableEntry{
    //Field
    private ArrayList<SymbolTableEntry> content = new ArrayList<>();
    private Integer nestingLevel;   //if we're working with arrays, this will tell us how many layers of subarrays there are i.e. [a,b,c] has NL=0 and [[a,b],[c]] has NL=1. [a,b,[c]] is not allowed.

    //Constructor
    public ArraySymbol(String type, Integer nestingLevel){
        setType(type);
        this.nestingLevel = nestingLevel;
    }

    public ArraySymbol(ArraySymbol other){  //create a copy of a given ComplexSymbol{
        //use the type to figure out which symbol type we're dealing with and create a copy of that type
        for(SymbolTableEntry entry : other.getContent()){
            if(entry instanceof IntegerSymbol){
                content.add(new IntegerSymbol((IntegerSymbol) entry));
            } else if(entry instanceof BooleanSymbol){
                content.add(new BooleanSymbol((BooleanSymbol) entry));
            } else if(entry instanceof StringSymbol){
                content.add(new StringSymbol((StringSymbol) entry));
            } else if(entry instanceof ArraySymbol){
                content.add(new ArraySymbol((ArraySymbol) entry));
            } else if(entry instanceof TemplateSymbol){
                content.add(new TemplateSymbol((TemplateSymbol) entry));
            } else {
                throw new UndefinedTypeException("Type \"" + entry.getType() + "\" is not defined!");
            }
        }
        this.nestingLevel = other.getNestingLevel();
        setType(other.getType());
    }

    //Method
    public ArrayList<SymbolTableEntry> getContent(){
        return content;
    }

    public void setContent(ArrayList<SymbolTableEntry> content){
        this.content = content;
    }

    public void addContent(SymbolTableEntry entry){
        content.add(entry);
    }

    public String getComplexType(){
        return "Array";
    }

    //if the nesting level of an ArraySymbol is 0, then its content is not nested arrays and indexing it will return a non-array symbol.
    //if the nesting level is greater than 0, then indexing the array contains another nested array.
    //this is useful to know because arrays with different nesting levels are different data types!
    // public Integer calculateNestingLevel(SymbolTableEntry entry){
    //     if (entry.getComplexType().equals("Array")) {
    //         ArraySymbol array = (ArraySymbol) entry;    //if the complexType is "Array", we should be able to cast like this
    //         return 1 + calculateNestingLevel(array.getContent().get(0));
    //     } else {
    //         return 0;
    //     }
    // }

    public Integer getNestingLevel(){
        return nestingLevel;
    }

    //returns true if the types, complex types, and nesting levels of two arrays are equal. Otherwise returns false
    public boolean compareTypes(ArraySymbol other){
        return (
            getType().equals(other.getType())
            && getComplexType().equals(other.getComplexType())
            && nestingLevel.equals(other.getNestingLevel())
        );
    }
}
//if we are not joining together subarrays, we know the array has nesting level 0
/*

    public ArraySymbol(ArrayList<ArraySymbol> subArrays){
        for (ArraySymbol subArray : subArrays) {
            addContent(subArray);
        }
        setType(subArrays.get(0).getType());    //when calling this constructor, we assume the subarrays have all been type checked
        nestingLevel = subArrays.get(0).getNestingLevel() + 1;
    }


 */