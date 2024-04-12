package com.proj4.AST.visitors;

import com.proj4.AST.nodes.*;

public interface NodeVisitor {
    public void visit(AST node);
}
