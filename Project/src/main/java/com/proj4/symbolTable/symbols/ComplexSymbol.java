package com.proj4.symbolTable.symbols;

import java.util.ArrayList;

public abstract class ComplexSymbol extends SymbolTableEntry{
    
    public abstract ArrayList<SymbolTableEntry> getContent();

    public abstract void setContent(ArrayList<SymbolTableEntry> content);

    public abstract void addContent(SymbolTableEntry entry);
}
