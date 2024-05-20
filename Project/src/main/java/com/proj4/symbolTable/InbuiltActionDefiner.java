package com.proj4.symbolTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.proj4.symbolTable.symbols.ActionSymbol;
import com.proj4.symbolTable.symbols.SymbolTableEntry;
import com.proj4.symbolTable.symbols.TemplateSymbol;

public class InbuiltActionDefiner {
    // Field 
    private static InbuiltActionDefiner definerInstance;
    private ArrayList<ActionSymbol> inbuiltActions;
    private ArrayList<String> map;

    // Constructor 
    private InbuiltActionDefiner(){
        map.add("RESULT");
        defineActions();
    }

    // Method 
    public static InbuiltActionDefiner getDefinerInstance() {
        if(definerInstance == null){
            definerInstance = new InbuiltActionDefiner();
        }
        return definerInstance;
    }

    public ArrayList<ActionSymbol> getInbuiltActions(){
        return inbuiltActions;
    }

    public ArrayList<String> getMap(){
        return map;
    }

    //these actions are hardcoded anyways, so we can get away with doing it stupid
    private void defineActions(){ 
        // TODO: Here we could have a "Type"-object that collects type, complextype, and nestinglevel into one convenient package
        // TODO: This would require a big overhaul of most of the type checker
        // TODO: add print - USE CONVERSION NODE TO PRINT OUTPUT?
        defineAction("setState", "String", "Primitive", Arrays.asList(""));
        defineAction("remove", "Null", "Null");
        defineAction("shuffle", "Null", "Null");
        defineAction("write", "Null", "Null");
    }

    private void defineAction(String functionName, String returnType, String complexReturnType, ArrayList<String> paramNames, ArrayList<SymbolTableEntry> startingParams){
        ActionSymbol function = new ActionSymbol(returnType, complexReturnType, null, 0);
        function.setParameterNames(paramNames);

        // Give actions initial scope
        function.setInitialScope(new Scope()); 

        for (int i = 0; i < startingParams.size(); i++) {
            function.getInitialScope().declareVariable(paramNames.get(i), startingParams.get(i));
        }
    
        TemplateSymbol functionBlueprint = new TemplateSymbol();
        functionBlueprint.addContent(
            SymbolTableEntry.instantiateDefault(
                returnType,
                complexReturnType,
                0
            )
        );
        // Put template, blueprint and action symbol in appropriate tables. 
        GlobalScope.getInstance().getBlueprintTable().put(functionName, functionBlueprint);
        GlobalScope.getInstance().getActionTable().put(functionName, function);
        GlobalScope.getInstance().getTemplateMapTable().put(functionName, map);
    }
}
