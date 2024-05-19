package com.proj4.symbolTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.proj4.symbolTable.symbols.ActionSymbol;
import com.proj4.symbolTable.symbols.RuleSymbol;
import com.proj4.symbolTable.symbols.StateSymbol;
import com.proj4.symbolTable.symbols.TemplateSymbol;

//this class represents the global scope of a DBL-program
public class GlobalScope {
    //Field
    //this class is a singleton:
    private static GlobalScope singleInstance;

    //Symbol tables
    //this table keeps track of template blueprints and their default values, to be used when instantiating them
    private HashMap<String, TemplateSymbol> blueprintTable = new HashMap<>();

    //this table keeps a map of every declared template in order to properly index them
    private HashMap<String, ArrayList<String>> templateMapTable = new HashMap<>();

    //this table keeps track of actions
    private HashMap<String, ActionSymbol> actionTable = new HashMap<>();

    //this table keeps track of which states have been declared in the current scope
    private HashMap<String, StateSymbol> stateTable = new HashMap<>();
  
    //this table keeps track of which rules have been declared 
    private HashMap<String, ArrayList<RuleSymbol>> ruleTable = new HashMap<>();

    // Inbuilt actions are hard coded
    private static ArrayList<String> inbuiltActions = new ArrayList<>(Arrays.asList("setState", "draw", "shuffle"));


    //Constructor 
    private GlobalScope(){

    }
    
    //Method
    //get the instance of this class and create it if it doesn't exist yet
    public static GlobalScope getInstance(){
        if (singleInstance == null) {
            singleInstance = new GlobalScope();
        }
        return singleInstance;
    }

    public HashMap<String, ActionSymbol> getActionTable(){
        return actionTable;
    }

    public HashMap<String, TemplateSymbol> getBlueprintTable(){
        return blueprintTable;
    }

    public HashMap<String, ArrayList<String>> getTemplateMapTable(){
        return templateMapTable;
    }

    public HashMap<String, StateSymbol> getStateTable(){
        return stateTable;
    }

    public HashMap<String, ArrayList<RuleSymbol>> getRuleTable(){
        return ruleTable;
    }

    public void setActionTable(HashMap<String, ActionSymbol> table){
        actionTable = table;
    }

    public void setBlueprintTable(HashMap<String, TemplateSymbol> table){
        blueprintTable = table;
    }

    public void setStateTable(HashMap<String, StateSymbol> table){
        stateTable = table;
    }

    public void setRuleTable(HashMap<String, ArrayList<RuleSymbol>> table){
        ruleTable = table;
    }

    public void declareRule(String triggerAction, RuleSymbol ruleSymbol){
        if (ruleTable.get(triggerAction) == null) { //assuming we don't need to check if the arraylist is empty
            ruleTable.put(triggerAction, new ArrayList<RuleSymbol>());
        }
        ruleTable.get(triggerAction).add(ruleSymbol);
    }

    public void declareState(String identifier, StateSymbol stateSymbol){
        stateTable.put(identifier, stateSymbol);
    }

    public ArrayList<String> getInbuiltActions(){
        return inbuiltActions;
    }

}
