package com.proj4.symbolTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.proj4.symbolTable.symbols.*;

public class InbuiltActionDefiner {
    // Field
    private static InbuiltActionDefiner definerInstance;
    private ArrayList<ActionSymbol> inbuiltActions;
    private ArrayList<String> map;
    private HashSet<String> actionIdentifiers;

    // Constructor
    private InbuiltActionDefiner(){
        map = new ArrayList<String>();
        actionIdentifiers = new HashSet<String>();
        map.add("RESULT");
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
    public void defineActions(){
        // TODO: Here we could have a "Type"-object that collects type, complextype, and nestinglevel into one convenient package
        // TODO: This would require a big overhaul of most of the type checker
        // TODO: add print - USE CONVERSION NODE TO PRINT OUTPUT?
        defineAction("setState", "String", "Primitive", new ArrayList<String>(Arrays.asList("state")), new ArrayList<SymbolTableEntry>(Arrays.asList(new StringSymbol(""))));
        //defineAction("remove", "Null", "Null");
        //defineAction("shuffle", "Null", "Null");
        defineAction("write", "Null", "Null", new ArrayList<String>(Arrays.asList("toWrite")), new ArrayList<SymbolTableEntry>(Arrays.asList(new StringSymbol(""))));
        defineAction("sizeInt", "Integer", "Primitive", new ArrayList<String>(Arrays.asList("array")), new ArrayList<SymbolTableEntry>(Arrays.asList(new ArraySymbol("Integer", 0))));
    }

    private void defineAction(String actionID, String returnType, String complexReturnType, ArrayList<String> paramNames, ArrayList<SymbolTableEntry> startingParams){
        ActionSymbol actionSymbol = new ActionSymbol(returnType, complexReturnType, null, -1);
        actionSymbol.setParameterNames(paramNames);

        // Give actions initial scope
        actionSymbol.setInitialScope(new Scope());

        for (int i = 0; i < startingParams.size(); i++) {
            actionSymbol.getInitialScope().declareVariable(paramNames.get(i), startingParams.get(i));
        }

        TemplateSymbol blueprint = new TemplateSymbol();
        blueprint.setType(actionID);
        blueprint.addContent(
            SymbolTableEntry.instantiateDefault(
                returnType,
                complexReturnType,
                -1
            )
        );
        // Put template, blueprint and action symbol in appropriate tables.
        GlobalScope.getInstance().getBlueprintTable().put(actionID, blueprint);
        GlobalScope.getInstance().getActionTable().put(actionID, actionSymbol);
        GlobalScope.getInstance().getTemplateMapTable().put(actionID, map);
        GlobalScope.getInstance().getResultTable().put(actionID, blueprint);
        // Put identifier in list of inbuilt action identifiers
        actionIdentifiers.add(actionID);
    }

    public HashSet<String> getIdentifiers(){
        return actionIdentifiers;
    }
}
