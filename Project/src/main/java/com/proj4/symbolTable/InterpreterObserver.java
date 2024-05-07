package com.proj4.symbolTable;

import java.util.Stack;

public interface InterpreterObserver {
    public void setCurrentScope(Stack<Scope> stackToCopy);
}
