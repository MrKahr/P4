package com.proj4.symbolTable;

import java.util.HashMap;

class TemplateType extends Type {
    private HashMap<String, Type> fields = new HashMap<>();

    public TemplateType(String name) {
        super(name);
    }

    public void addField(String fieldName, Type fieldType) {
        fields.put(fieldName, fieldType);
    }
}
