package com.proj4.AST.visitors.InterpreterVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Assignment;
import com.proj4.AST.nodes.TemplateDecl;
import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.symbolTable.Scope;
import com.proj4.symbolTable.symbols.SymbolTableEntry;
import com.proj4.symbolTable.symbols.TemplateSymbol;
import com.proj4.AST.nodes.*;

import java.util.ArrayList;


public class TemplateDeclInterpreter extends InterpreterVisitor {

    public void visit(AST node) {
        TemplateDecl templateDecl  = (TemplateDecl) node;
        TemplateSymbol blueprint = Scope.getBlueprintTable().get(templateDecl.getIdentifier());
        ArrayList<SymbolTableEntry> content = blueprint.getContent();

        for (int index = 0; index < templateDecl.getChildren().size(); index++) {
            Assignment assignment = ((Declaration)templateDecl.getChild(index)).getInitialAssignment();
            if(assignment != null){
                assignment.visitChild(new InterpreterDecider(), assignment.getValueExpression());
                blueprint.getContent().set(index,InterpreterVisitor.getReturnSymbol());
            }
        }
    }
}