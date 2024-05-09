package com.proj4.symbolTable.symbols;

import java.util.ArrayList;

import com.proj4.AST.nodes.AST;

public class StateSymbol{
    //Field
    private AST body;
    private ArrayList<String> actionList;
    
    //Constructor
    public StateSymbol(AST body, ArrayList<String> actionList){    //content list is created by default
        this.body = body;
        this.actionList = actionList;
    }    

    //Method
    public AST getBody() {
        return body;
    }

    public String getComplexType(){
        return "State";
    }

    public ArrayList<String> getActionList(){
        return actionList;
    }
}
