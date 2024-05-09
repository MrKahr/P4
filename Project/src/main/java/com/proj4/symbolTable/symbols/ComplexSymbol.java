package com.proj4.symbolTable.symbols;

import java.util.ArrayList;

public abstract class ComplexSymbol extends SymbolTableEntry{
    
    public abstract ArrayList<SymbolTableEntry> getContent();

    public abstract void setContent(ArrayList<SymbolTableEntry> content);

    public abstract void addContent(SymbolTableEntry entry);

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other instanceof ComplexSymbol && ((ComplexSymbol)other).getContent().size() == getContent().size()) {
            for (int i = 0; i < getContent().size(); i++) {
                if (!((ComplexSymbol)other).getContent().get(i).equals(getContent().get(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
