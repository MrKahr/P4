package com.proj4.symbolTable;

import com.proj4.antlrClass.DBLBaseVisitor;
import com.proj4.antlrClass.DBLParser;
import com.proj4.exceptions.UndefinedTypeException;

public class DeclarationVisitor extends DBLBaseVisitor<Type> {
    private SymbolTable symbolTable = new SymbolTable();

    // @Override
    // public Type visitDeclaration(DBLParser.DeclarationContext ctx) {
    //     String varName = ctx.IDENTIFIER().getText();        // Gets name of variable by its identifier from the declaration context
    //     String typeName = ctx.type_primitive().getText();   // Same for type
    //     Type type = symbolTable.resolveType(typeName);      // Check if the type has been defined previously

    //     if (type == null) {
    //         // Throw exception for undefined primitives
    //         throw new UndefinedTypeException(typeName);
    //     }

    //     // Add variable to symbol table in current scope
    //     symbolTable.define(varName, type);
    //     return type; // Type might be used later
    // }

    // @Override
    // public Type visitTemplate_decl(DBLParser.Template_declContext ctx) {
    //     String templateName = ctx.typedef_user().getText();
    //     ComplexType templateType = new ComplexType(templateName);

    //     // Iterate over attributes of Template
    //     for (DBLParser.DeclarationContext declCtx : ctx.declaration_list().declaration()) {
    //         String fieldName = declCtx.IDENTIFIER().getText();
    //         String fieldType = declCtx.type_primitive().getText(); // Currently does not handle Templates in other Templates
    //         templateType.addField(fieldName, new PrimitiveType(fieldType));
    //     }

    //     symbolTable.defineType(templateName, templateType);
    //     return templateType;
    // }
}