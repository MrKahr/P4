package com.proj4.AST.visitors;

import com.proj4.AST.nodes.*;
import com.proj4.AST.visitors.InterpreterVisitors.*;
import com.proj4.symbolTable.Scope;

public class InterpreterDecider implements VisitorDecider {
    private static Boolean verbose = false;

    public InterpreterDecider(){}

    public InterpreterDecider(Boolean isVerbose) {
        verbose = isVerbose;
    }

    //decide which visitor class to use for the given node
     public void decideVisitor(AST node){
        if(verbose){
            System.out.println("\n\nInterpreting " + node.getClass().getSimpleName() + ".");
            if (!Scope.getScopeStack().empty()) {
                Scope.getCurrent().printBindings();
            }
        }
        switch (node.getClass().getSimpleName()) {
            case "ActionCall":
                node.acceptVisitor(new ActionCallInterpreter());
                break;
            case "ActionDecl":
                node.acceptVisitor(new ActionDeclInterpreter());
                break;
            case "ArrayInstance":
                node.acceptVisitor(new ArrayInstanceInterpreter());
                break;
            case "Assignment":
                node.acceptVisitor(new AssignmentInterpreter(verbose));
                break;
            case "Expression":
                node.acceptVisitor(new ExpressionInterpreter(verbose));
                break;
            case "ForLoop":
                node.acceptVisitor(new ForLoopInterpreter());
                break;
            case "IfElse":
                node.acceptVisitor(new IfElseInterpreter(verbose));
                break;
            case "Declaration":
                node.acceptVisitor(new DeclarationInterpreter());
                break;
            case "Program":
                node.acceptVisitor(new ProgramInterpreter());
                break;
            case "Return":
                node.acceptVisitor(new ReturnInterpreter(verbose));
                break;
            case "RuleDecl":
                node.acceptVisitor(new RuleDeclInterpreter());
                break;
            case "StateDecl":
                node.acceptVisitor(new StateDeclInterpreter());
                break;
            case "TemplateDecl":
                node.acceptVisitor(new TemplateDeclInterpreter());
                break;
            case "TemplateInstance":
                node.acceptVisitor(new TemplateInstanceInterpreter());
                break;
            case "Body":
                node.acceptVisitor(new BodyInterpreter());
                break;
            case "Variable":
                node.acceptVisitor(new VariableInterpreter(verbose));
                break;
            default:
                System.err.println("UNRECOGNIZED NODE TYPE!\nGOT " + node.getClass().getSimpleName());
                break;
        }
    }
}
