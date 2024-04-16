package com.proj4.AST.visitors;

import com.proj4.AST.nodes.*;

public interface VisitorDecider {
    //decide which visitor class to use for the given node
    public void decideVisitor(AST node);

}
