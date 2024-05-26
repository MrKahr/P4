package com.proj4.AST.visitors;

import com.proj4.AST.nodes.*;
import com.proj4.AST.visitors.CheckVisitors.*;
import com.proj4.symbolTable.ScopeManager;

public class CheckDecider implements VisitorDecider {
    private static Boolean verbose = false;

    public CheckDecider(){}

    public CheckDecider(Boolean isVerbose){
        verbose = isVerbose;
    }
    //decide which visitor class to use for the given node
     public void decideVisitor(AST node){
        if(verbose){
            System.out.println("\n\nType checking " + node.getClass().getSimpleName() + ".");
            if (!ScopeManager.getInstance().getScopeStack().empty()) {
                ScopeManager.getInstance().printBindings();
            }
        }
        switch (node.getClass().getSimpleName()) {
            case "ActionCall":
                node.acceptVisitor(new ActionCallTypeChecker());
                break;
            case "ActionDecl":
                node.acceptVisitor(new ActionDeclTypeChecker(verbose));
                break;
            case "ArrayInstance":
                node.acceptVisitor(new ArrayInstanceTypeChecker());
                break;
            case "Assignment":
                node.acceptVisitor(new AssignmentTypeChecker());
                break;
            case "Expression":
                node.acceptVisitor(new ExpressionTypeChecker());
                break;
            case "ForLoop":
                node.acceptVisitor(new ForLoopTypeChecker());
                break;
            case "IfElse":
                node.acceptVisitor(new IfElseTypeChecker());
                break;
            case "Declaration":
                node.acceptVisitor(new DeclarationTypeChecker());
                break;
            case "Program":
                node.acceptVisitor(new ProgramTypeChecker());
                break;
            case "Return":
                node.acceptVisitor(new ReturnTypeChecker(verbose));
                break;
            case "RuleDecl":
                node.acceptVisitor(new RuleDeclTypeChecker(verbose));
                break;
            case "StateDecl":
                node.acceptVisitor(new StateDeclTypeChecker());
                break;
            case "TemplateDecl":
                node.acceptVisitor(new TemplateDeclTypeChecker(verbose));
                break;
            case "TemplateInstance":
                node.acceptVisitor(new TemplateInstanceTypeChecker());
                break;
            case "Body":
                node.acceptVisitor(new BodyTypeChecker());
                break;
            case "Variable":
                node.acceptVisitor(new VariableTypeChecker());
                break;
            default:
                System.err.println("UNRECOGNIZED NODE TYPE!\nGOT " + node.getClass().getSimpleName());
                break;
        }
    }
}
