package com.proj4.AST.visitors.InterpreterVisitors;

import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.symbolTable.GlobalScope;
import com.proj4.symbolTable.symbols.SymbolTableEntry;
import com.proj4.symbolTable.symbols.TemplateSymbol;
import com.proj4.AST.nodes.*;

import java.util.ArrayList;


public class TemplateDeclInterpreter implements NodeVisitor {

    public void visit(AST node) {
        TemplateDecl templateDecl  = (TemplateDecl) node;
        TemplateSymbol blueprint = GlobalScope.getInstance().getBlueprintTable().get(templateDecl.getIdentifier());
        ArrayList<SymbolTableEntry> content = blueprint.getContent();

        for (int index = 0; index < templateDecl.getChildren().size(); index++) {
            Declaration decl  = (Declaration)templateDecl.getChild(index);

            if(decl.getChildren().size() != 0){
                Assignment assignment = decl.getInitialAssignment();
                assignment.visitChild(new InterpreterDecider(), assignment.getValueExpression());
                content.set(index,InterpreterVisitor.getInstance().getReturnSymbol());
                // TEMPLATE CHILDREN NOT 0 HERE FOR C3
            }
        }
    }
}