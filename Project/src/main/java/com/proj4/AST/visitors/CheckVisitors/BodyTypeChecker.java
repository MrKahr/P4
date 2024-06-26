package com.proj4.AST.visitors.CheckVisitors;

import com.proj4.AST.nodes.AST;
import com.proj4.AST.nodes.Body;
import com.proj4.AST.visitors.CheckDecider;
import com.proj4.AST.visitors.NodeVisitor;
import com.proj4.symbolTable.ScopeManager;

//the body-node exists to prevent synthesis of scopes in situations where that would be a problem.
//consider for example the IfElse-node. If it did not use body-nodes,
//a declaration in its then-block would create a variable that is accessible in its else-block.
//we obviously want to avoid this.
public class BodyTypeChecker implements NodeVisitor{
    public void visit(AST node){
        Body body = (Body) node;
        ScopeManager.getInstance().inherit();

        body.visitChildren(new CheckDecider());
        //make sure to remove any effect of the body before continuing
        //this shouldn't be done when interpreting!
        ScopeManager.getInstance().synthesize();
    }
}