package com.proj4.AST;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CommonToken;    // Implements the Token interface



// Homogeneous AST node type ---- Language Implementation Patterns, p. 109
public class AST { 
    CommonToken token; // From which token did we create node?
    List<AST> children; // normalized list of children
    
    public AST() { ; } // for making nil-rooted nodes
    
    public AST(CommonToken token) { 
        this.token = token; 
    }

    /** Create node from token type; used mainly for imaginary tokens */
    public AST(int tokenType) { 
        this.token = new CommonToken(tokenType);
    }


    /** External visitors execute the same action for all nodes
    * with same node type while walking. */
    public int getNodeType() { 
        return token.getType(); 
    }
    
    public void addChild(AST t) {
        if ( children==null ) children = new ArrayList<AST>();
            children.add(t);
    }

    public boolean isNil() { 
        return token==null;
    }
}