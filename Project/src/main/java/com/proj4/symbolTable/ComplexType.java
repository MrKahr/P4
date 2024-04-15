package com.proj4.symbolTable;

import java.util.HashMap;
//both templates and actions are complex types.
//templates have a type and contain a number of fields, which also have types.
//and actions return some type (which could be null) and require arguments, which have types.
//we can differentiate between templates and actions 
//because we will always place templates in a variable table and actions in a function table.
class ComplexType extends Type {
    private HashMap<String, Type> fields = new HashMap<>();

    public ComplexType(String name) {
        super(name);
    }

    public void addField(String fieldName, Type fieldType) {
        fields.put(fieldName, fieldType);
    }
}
