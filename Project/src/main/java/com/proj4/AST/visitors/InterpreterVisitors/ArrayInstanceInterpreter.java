package com.proj4.AST.visitors.InterpreterVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.ArrayInstance;
import com.proj4.AST.visitors.InterpreterDecider;
import com.proj4.AST.visitors.InterpreterVisitor;
import com.proj4.symbolTable.symbols.ArraySymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;

public class ArrayInstanceInterpreter extends InterpreterVisitor {

    public void visit(AST node) {
        ArrayInstance arrayInstance = (ArrayInstance) node;

        //use the first child to decide the type and nesting levl
        arrayInstance.visitChild(new InterpreterDecider(), 0);
        SymbolTableEntry child = InterpreterVisitor.getReturnSymbol();
        ArraySymbol array;
        if (child.getComplexType().equals("Array")) {
            array = new ArraySymbol(child.getType(), ((ArraySymbol) child).getNestingLevel() + 1);
        } else {
            array = new ArraySymbol(child.getType(), 0);
        }
        //add the first child to the content list
        array.addContent(child);

        //now add the rest of the children if there are any
        for (int i = 1; i < arrayInstance.getChildren().size(); i++) {
            arrayInstance.visitChild(new InterpreterDecider(), i);
            array.addContent(InterpreterVisitor.getReturnSymbol());    
        }

        //return the array
        InterpreterVisitor.setReturnSymbol(array);
    }
}
