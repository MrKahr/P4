package com.proj4.symbolTable.symbols;

import java.util.ArrayList;

//templates, actions, and arrays are all complex types.
//templates have a type and contain a number of fields, which also have types.
//and actions return some type (which could be null) and require arguments, which have types.
//arrays have a type and contain some content which has the same type.
//the difference between arrays, templates, and actions is how we type check them and in which symbol tables we place them.
public class ComplexSymbol extends SymbolTableEntry{
    //Field
    private ArrayList<SymbolTableEntry> content = new ArrayList<>();

    //Constructor
    public ComplexSymbol(){}    //content list is created by default

    public ComplexSymbol(ComplexSymbol other){  //create a copy of a given ComplexSymbol
        for (SymbolTableEntry entry : other.getContent()) {
            //use the type to figure out which symbol type we're dealing with and create a copy of that type
            switch (entry.getType()) {
                case "Integer": //IntSymbol
                    content.add(new IntSymbol((IntSymbol) entry));
                    break;
                case "Boolean": //BooleanSymbol
                    content.add(new BooleanSymbol((BooleanSymbol) entry));
                    break;
                case "String":  //StringSymbol
                    content.add(new StringSymbol((StringSymbol) entry));
                    break;
                default:        //ComplexSymbol, since the type is any other type than the primitives
                    content.add(new ComplexSymbol((ComplexSymbol) entry));
                    break;
            }
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
}
