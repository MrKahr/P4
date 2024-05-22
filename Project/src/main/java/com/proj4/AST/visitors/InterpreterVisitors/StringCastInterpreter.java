package com.proj4.AST.visitors.InterpreterVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.StringCast;
import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.exceptions.ParameterMismatchExpection;
import com.proj4.symbolTable.symbols.PrimitiveSymbol;
import com.proj4.symbolTable.symbols.StringSymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

public class StringCastInterpreter implements NodeVisitor {

    @SuppressWarnings("rawtypes")
    public void visit(AST node) {
        StringCast stringCast = (StringCast) node;
        stringCast.visitChild(new InterpreterDecider(), stringCast.getSubtree());

        SymbolTableEntry toConvert = InterpreterVisitor.getInstance().getReturnSymbol();
        
        try {
            //make a string out of the returned symbol and set it as the new return symbol
            StringSymbol toReturn = new StringSymbol(((PrimitiveSymbol)toConvert).toString());
            InterpreterVisitor.getInstance().setReturnSymbol(toReturn);
        } catch (ClassCastException cce) {
            throw new ParameterMismatchExpection("Sorry, StringCast currently only works for PrimitiveSymbols :/");
        }
    }
}