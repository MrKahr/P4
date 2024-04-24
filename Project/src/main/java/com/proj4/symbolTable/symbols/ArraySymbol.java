package com.proj4.symbolTable.symbols;

import java.util.ArrayList;

import com.proj4.exceptions.UndefinedTypeException;

public class ArraySymbol extends SymbolTableEntry{
    //Field
    private ArrayList<SymbolTableEntry> content = new ArrayList<>();

    //Constructor
    public ArraySymbol(String type){    //content list is created by default
        setType(type);
    }    

    public ArraySymbol(ArraySymbol other){  //create a copy of a given ComplexSymbol{
            //use the type to figure out which symbol type we're dealing with and create a copy of that type
            switch (getType()) {
                case "Integer": //IntSymbol
                    for (SymbolTableEntry entry : other.getContent()) {
                        content.add(new IntSymbol((IntSymbol) entry));
                    }
                    break;
                case "Boolean": //BooleanSymbol
                    for (SymbolTableEntry entry : other.getContent()) {
                        content.add(new BooleanSymbol((BooleanSymbol) entry));
                    }
                    break;
                case "String":  //StringSymbol
                    for (SymbolTableEntry entry : other.getContent()) {
                        content.add(new StringSymbol((StringSymbol) entry));
                    }
                    break;
                default:        //Not a primitive
                    switch (getComplexType()) {
                        case "Array":   //ArraySymbol
                            for (SymbolTableEntry entry : other.getContent()) {
                                content.add(new ArraySymbol((ArraySymbol) entry));
                            }
                            break;
                        case "Template":    //TemplateSymbol
                            for (SymbolTableEntry entry : other.getContent()) {
                                content.add(new TemplateSymbol((TemplateSymbol) entry));
                            }   
                            break;
                        default:
                            throw new UndefinedTypeException("Type \"" + getType() + "\" is not defined!");
                    }
                    break;
            }
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
}
